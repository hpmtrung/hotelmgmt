package vn.lotusviet.hotelmgmt.web.rest;

import com.vladmihalcea.concurrent.Retry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.annotation.security.CustomerSecured;
import vn.lotusviet.hotelmgmt.core.annotation.validation.email.EmailConstraint;
import vn.lotusviet.hotelmgmt.core.annotation.validation.langkey.LangKeyConstraint;
import vn.lotusviet.hotelmgmt.core.exception.InvalidParamException;
import vn.lotusviet.hotelmgmt.exception.AccountNotFoundException;
import vn.lotusviet.hotelmgmt.exception.RequestParamInvalidException;
import vn.lotusviet.hotelmgmt.model.dto.person.*;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;
import vn.lotusviet.hotelmgmt.service.AccountService;
import vn.lotusviet.hotelmgmt.service.mail.MailService;
import vn.lotusviet.hotelmgmt.service.storage.StorageService;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

import static vn.lotusviet.hotelmgmt.exception.ApplicationErrorCode.*;
import static vn.lotusviet.hotelmgmt.service.AccountService.AccountStatus;
import static vn.lotusviet.hotelmgmt.service.AccountService.AccountStatus.*;

@RestController
@Validated
@RequestMapping(value = AccountController.URL_PREFIX)
public class AccountController {

  public static final String URL_PREFIX = "/api/v1/account";

  private final AccountService accountService;
  private final MailService mailService;
  private final StorageService storageService;
  private final PasswordEncoder passwordEncoder;

  public AccountController(
      AccountService accountService,
      MailService mailService,
      StorageService storageService,
      PasswordEncoder passwordEncoder) {
    this.accountService = accountService;
    this.mailService = mailService;
    this.storageService = storageService;
    this.passwordEncoder = passwordEncoder;
  }

  @LogAround
  @GetMapping("/account_status")
  public ResponseEntity<Object> getAccountStatusByEmail(
      @RequestParam @EmailConstraint String email) {
    AccountStatus status = accountService.getAccountStatusByEmail(email);
    return ResponseEntity.ok(Map.of("status", status));
  }

  @LogAround(output = false)
  @PostMapping("/reset_password/init")
  public ResponseEntity<Object> sendPasswordResetMail(
      final @RequestParam @EmailConstraint String email) {
    verifyEmailAccountIsActivated(email);

    Account account = accountService.getActivatedAccountByEmail(email).orElseThrow();
    accountService.updateAccountPasswordResetKey(account);
    mailService.sendPasswordResetMail(account);
    return ResponseEntity.accepted().build();
  }

  @LogAround
  @Retry(on = OptimisticLockException.class)
  @PostMapping("/reset_password/finish")
  public void completePasswordReset(final @Valid @RequestBody KeyAndPasswordDto keyAndPassword) {
    final String newPassword = keyAndPassword.getNewPassword();
    final String resetKey = keyAndPassword.getKey();
    final Account account =
        accountService
            .getAccountByPasswordResetKey(resetKey)
            .orElseThrow(
                () ->
                    InvalidParamException.builder()
                        .body(
                            PASSWORD_RESET_KEY_INVALID,
                            "key",
                            resetKey,
                            "Reset key is not found, or expired.")
                        .build());
    accountService.updateAccountPassword(account, newPassword);
  }

  @CustomerSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/update_langkey")
  public void updateLoginAccountLangKey(final @RequestParam @LangKeyConstraint String langKey) {
    Account account = accountService.getLoginAccount();
    accountService.updateAccountLangKey(account, langKey);
  }

  @CustomerSecured
  @LogAround(output = false)
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/change_password")
  public void updateLoginAccountPassword(
      final @Valid @RequestBody PasswordChangeDto passwordChange) {
    String currentPassword = passwordChange.getCurrentPassword();
    String newPassword = passwordChange.getNewPassword();

    Account account = accountService.getLoginAccount();
    boolean isPassordNotMatched = !isAccountPasswordMatched(account, currentPassword);
    if (isPassordNotMatched) {
      throw InvalidParamException.builder()
          .body(
              CURRENT_PASSWORD_NOT_MATCHED,
              "currentPassword",
              currentPassword,
              "Current password is not matched.")
          .build();
    }
    accountService.updateAccountPassword(account, newPassword);
  }

  @CustomerSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/update_profile")
  public void updateLoginAccountProfile(
      final @Valid @RequestBody AccountProfileUpdateDto accountProfileUpdateDto) {
    Account account = accountService.getLoginAccount();
    accountService.updateAccountProfile(account, accountProfileUpdateDto);
  }

  @CustomerSecured
  @LogAround
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/update_image")
  public ResponseEntity<Object> updateImage(@RequestParam MultipartFile image) {
    Account account = accountService.getLoginAccount();
    accountService.updateAccountImage(account, image);
    return ResponseEntity.ok(
        Collections.singletonMap("url", storageService.getFileURL(account.getImageURL())));
  }

  @LogAround
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/register")
  public void registerNewCustomerAccount(
      final @Valid @RequestBody CustomerRegisterDto customerRegisterDto) {
    String email = customerRegisterDto.getEmail();
    AccountStatus status = accountService.getAccountStatusByEmail(email);

    if (status.equals(ACTIVATED)) {
      throw InvalidParamException.builder()
          .body(
              CustomerRegisterDto.DOMAIN_NAME + ".email.exist", "email", email, "Email is existed")
          .build();
    }

    Account account = accountService.saveCustomerAccountFromRegistration(customerRegisterDto);
    mailService.sendActivationEmail(account);
  }

  @LogAround
  @Retry(on = OptimisticLockException.class)
  @GetMapping("/activate")
  public void activateRegisterAccount(
      final @RequestParam long accountId, final @RequestParam String key) {
    AccountDto account =
        accountService
            .getAccountById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

    if (account.isActivated()) {
      throw new RequestParamInvalidException("Account id is not found", "accountId", "notFound");
    }
    if (accountService.activateAccountByActivationKey(key).isEmpty()) {
      throw new RequestParamInvalidException("Key is not found", "key", "notFound");
    }
  }

  private boolean isAccountPasswordMatched(final Account account, final String password) {
    return passwordEncoder.matches(password, account.getHashedPassword());
  }

  private void verifyEmailAccountIsActivated(final String email) {
    AccountStatus status = accountService.getAccountStatusByEmail(email);

    if (status.equals(NOT_EXIST)) {
      throw InvalidParamException.builder().body(EMAIL_NOT_EXIST, "email", email).build();
    } else if (status.equals(NOT_ACTIVATED)) {
      throw InvalidParamException.builder().body(EMAIL_NOT_ACTIVATED, "email", email).build();
    }
  }
}