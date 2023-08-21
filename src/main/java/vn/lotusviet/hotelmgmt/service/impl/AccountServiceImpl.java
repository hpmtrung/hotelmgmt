package vn.lotusviet.hotelmgmt.service.impl;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.annotation.validation.image.ValidImage;
import vn.lotusviet.hotelmgmt.core.exception.auth.SessionUnavailableException;
import vn.lotusviet.hotelmgmt.exception.EmailDuplicateException;
import vn.lotusviet.hotelmgmt.exception.entity.AuthorityNotFoundException;
import vn.lotusviet.hotelmgmt.model.dto.person.*;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;
import vn.lotusviet.hotelmgmt.model.entity.person.Authority;
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;
import vn.lotusviet.hotelmgmt.repository.person.AccountRepository;
import vn.lotusviet.hotelmgmt.repository.person.AuthorityRepository;
import vn.lotusviet.hotelmgmt.service.AccountService;
import vn.lotusviet.hotelmgmt.service.SystemService;
import vn.lotusviet.hotelmgmt.service.factory.AccountFactory;
import vn.lotusviet.hotelmgmt.service.mail.MailService;
import vn.lotusviet.hotelmgmt.service.mapper.AccountMapper;
import vn.lotusviet.hotelmgmt.service.storage.StorageService;
import vn.lotusviet.hotelmgmt.util.FileUtil;
import vn.lotusviet.hotelmgmt.util.SecurityUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleKey.CUSTOMER_ACCOUNT_RESET_PW_TIMEOUT_MINUTES;
import static vn.lotusviet.hotelmgmt.service.storage.StorageService.FileBrand.UPLOAD_IMG;
import static vn.lotusviet.hotelmgmt.service.storage.StorageService.FileCategory.USER;
import static vn.lotusviet.hotelmgmt.service.storage.StorageService.FileEntry;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

  private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

  private final AccountRepository accountRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthorityRepository authorityRepository;
  private final AccountMapper accountMapper;
  private final MailService mailService;
  private final SystemService systemService;
  private final StorageService storageService;
  private final CacheManager cacheManager;
  private final AccountFactory accountFactory;

  public AccountServiceImpl(
      AccountRepository accountRepository,
      PasswordEncoder passwordEncoder,
      AuthorityRepository authorityRepository,
      AccountMapper accountMapper,
      MailService mailService,
      SystemService systemService,
      StorageService storageService,
      CacheManager cacheManager,
      AccountFactory accountFactory) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
    this.authorityRepository = authorityRepository;
    this.accountMapper = accountMapper;
    this.mailService = mailService;
    this.systemService = systemService;
    this.storageService = storageService;
    this.cacheManager = cacheManager;
    this.accountFactory = accountFactory;
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public Optional<Account> getAccountByPasswordResetKey(final @NonNull String key) {
    Objects.requireNonNull(key);
    return accountRepository
        .findByPwResetKeyAndActivatedIsTrue(key)
        .filter(this::isAccountResetDurationValid);
  }

  @Override
  @LogAround
  public void updateAccountPassword(
      final @NonNull Account account, final @NonNull String newPassword) {
    Objects.requireNonNull(account);
    Objects.requireNonNull(newPassword);
    account
        .setHashedPassword(passwordEncoder.encode(newPassword))
        .setPwResetKey(null)
        .setResetAt(null);
    clearAccountCache(account);
    accountRepository.save(account);
  }

  @LogAround
  public void updateAccountPasswordResetKey(final @NonNull Account account) {
    account.setPwResetKey(SecurityUtil.Random.generateResetKey()).setResetAt(LocalDateTime.now());
    clearAccountCache(account);
    accountRepository.save(account);
  }

  @LogAround
  @Transactional(readOnly = true)
  public Optional<Account> getActivatedAccountByEmail(final @NonNull String email) {
    return accountRepository.findByEmail(email).filter(Account::isActivated);
  }

  @Override
  @LogAround
  public void updateAccountLangKey(
      final @NonNull Account account, final @NonNull String newLangKey) {
    Objects.requireNonNull(account);
    Objects.requireNonNull(newLangKey);
    account.setLangKey(newLangKey);
    clearAccountCache(account);
    accountRepository.save(account);
  }

  @Override
  @LogAround
  public void updateAccountImage(
      final @NonNull Account account, final @NonNull @ValidImage MultipartFile multipartFile) {
    Objects.requireNonNull(account);
    Objects.requireNonNull(multipartFile);

    final String fileExtension = FileUtil.getFileExtension(multipartFile);

    String fileName = account.getId() + fileExtension;

    deleteAccountImage(account);

    String fileKey =
        storageService.saveFile(multipartFile, new FileEntry(fileName, UPLOAD_IMG, USER));

    account.setImageURL(fileKey);
    clearAccountCache(account);
    accountRepository.save(account);
    log.debug("Account image key after updated {}", fileKey);
  }

  @Override
  @LogAround
  public void updateAccountProfile(
      final @NonNull Account account, final @NonNull AccountProfileUpdateDto updatedProfile) {
    Objects.requireNonNull(account);
    Objects.requireNonNull(updatedProfile);

    accountMapper.partialUpdateAccountEntity(account, updatedProfile);
    clearAccountCache(account);
    accountRepository.save(account);
  }

  @Transactional(readOnly = true)
  public Account getLoginAccount() {
    return SecurityUtil.getCurrentAccountLogin()
        .flatMap(this::getActivatedAccountByEmail)
        .orElseThrow(SessionUnavailableException::new);
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public Optional<AccountDto> getAccountById(final long id) {
    return accountRepository.findById(id).map(accountMapper::toDto);
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public AccountStatus getAccountStatusByEmail(final String email) {
    final Account account = accountRepository.findByEmail(email).orElse(null);

    if (account == null) return AccountStatus.NOT_EXIST;
    if (!account.isActivated()) return AccountStatus.NOT_ACTIVATED;
    return AccountStatus.ACTIVATED;
  }

  @Override
  @Transactional(readOnly = true)
  public Authority getAuthorityByName(final AuthorityName name) {
    return authorityRepository
        .findByName(name)
        .orElseThrow(() -> new AuthorityNotFoundException(name));
  }

  @LogAround
  public Optional<Account> activateAccountByActivationKey(final String key) {
    return accountRepository
        .findByActivationKey(key)
        .map(
            account -> {
              account.setActivated(true).setActivationKey(null);
              clearAccountCache(account);
              accountRepository.save(account);
              return account;
            });
  }

  @Override
  @LogAround
  public Account saveCustomerAccountFromRegistration(final CustomerRegisterDto registerDto) {
    accountRepository
        .findByEmail(registerDto.getEmail())
        .ifPresent(this::removeNonActivatedAccount);

    Account newAccount =
        new Account()
            .setEmail(registerDto.getEmail())
            .setHashedPassword(passwordEncoder.encode(registerDto.getPassword()))
            .setFirstName(registerDto.getFirstName())
            .setLastName(registerDto.getLastName())
            .setActivated(false)
            .setLangKey(registerDto.getLangKey())
            .setActivationKey(SecurityUtil.Random.generateActivationKey())
            .setAuthority(getAuthorityByName(AuthorityName.ROLE_CUSTOMER));

    return accountRepository.save(newAccount);
  }

  @Override
  @LogAround
  public Account saveCustomerAccountIfNotExist(final CustomerDto customer) {
    Account existAccount = accountRepository.findByEmail(customer.getEmail()).orElse(null);

    if (existAccount == null) {
      existAccount = accountFactory.createAccountForCustomer(customer);
      existAccount = accountRepository.save(existAccount);
      mailService.sendPasswordResetMail(existAccount);
      log.debug("Save new account [{}] for customer [{}]", existAccount, customer);
    }

    return existAccount;
  }

  @Override
  @LogAround
  public Optional<Account> updateCustomerAccount(final CustomerDto customerDto) {
    return accountRepository
        .findByEmail(customerDto.getEmail())
        .map(
            account -> {
              account
                  .setFirstName(customerDto.getFirstName())
                  .setLastName(customerDto.getLastName())
                  .setPhone(customerDto.getPhone())
                  .setAddress(customerDto.getAddress());
              clearAccountCache(account);
              accountRepository.save(account);
              log.debug("Updating customer's account from reservation: {}", account);
              return account;
            });
  }

  @Override
  @LogAround
  public Account saveEmployeeAccount(
      final @NotNull Employee employee, final @NotNull EmployeeAccountCreateDto accountCreate) {

    if (accountRepository.existsByEmail(accountCreate.getEmail())) {
      throw new EmailDuplicateException();
    }

    Account newAccount = accountFactory.createAccountForEmployee(accountCreate, employee);

    newAccount = accountRepository.saveAndFlush(newAccount);
    // Send password reset email
    mailService.sendPasswordResetMail(newAccount);

    return newAccount;
  }

  @Override
  @LogAround
  public void updateEmployeeAccount(
      final Employee employee, final EmployeeAccountUpdateDto accountUpdate) {
    Objects.requireNonNull(employee);
    Objects.requireNonNull(accountUpdate);

    String updatedEmail = accountUpdate.getEmail();
    AuthorityName updatedAuthority = accountUpdate.getAuthority();

    final Account account = employee.getAccount();

    boolean isUpdatedEmailExist =
        updatedEmail != null
            && !updatedEmail.equalsIgnoreCase(account.getEmail())
            && accountRepository.existsByEmail(updatedEmail);
    if (isUpdatedEmailExist) {
      throw new EmailDuplicateException();
    }

    account.setEmail(updatedEmail);

    if (updatedAuthority != null) {
      account.setAuthority(getAuthorityByName(updatedAuthority));
    }

    clearAccountCache(account);
    accountRepository.save(account);
  }

  private void deleteAccountImage(final Account account) {
    if (account.getImageURL() != null) {
      storageService.deleteFiles(List.of(account.getImageURL()));
    }
  }

  private void removeNonActivatedAccount(final Account account) {
    if (account.isActivated()) return;
    clearAccountCache(account);
    accountRepository.delete(account);
  }

  private boolean isAccountResetDurationValid(final Account account) {
    return account
        .getResetAt()
        .isAfter(
            LocalDateTime.now()
                .minus(
                    systemService.getBusinessRuleValue(
                        CUSTOMER_ACCOUNT_RESET_PW_TIMEOUT_MINUTES, Integer.class),
                    ChronoUnit.MINUTES));
  }

  private void clearAccountCache(final Account account) {
    Objects.requireNonNull(account);
    Objects.requireNonNull(cacheManager.getCache(AccountRepository.CACHE_ACCOUNT_ENTITY_BY_ID))
        .evict(account.getId());
    Objects.requireNonNull(cacheManager.getCache(AccountRepository.CACHE_ACCOUNT_ENTITY_BY_EMAIL))
        .evict(account.getEmail());
  }
}