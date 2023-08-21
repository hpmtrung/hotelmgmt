package vn.lotusviet.hotelmgmt.web.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import vn.lotusviet.hotelmgmt.TestUtil;
import vn.lotusviet.hotelmgmt.WithCustomerRoleMockUser;
import vn.lotusviet.hotelmgmt.core.exception.ErrorDetail;
import vn.lotusviet.hotelmgmt.model.dto.person.AccountProfileUpdateDto;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerRegisterDto;
import vn.lotusviet.hotelmgmt.model.dto.person.KeyAndPasswordDto;
import vn.lotusviet.hotelmgmt.model.dto.person.PasswordChangeDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;
import vn.lotusviet.hotelmgmt.repository.person.AccountRepository;
import vn.lotusviet.hotelmgmt.service.AccountService;
import vn.lotusviet.hotelmgmt.service.AccountService.AccountStatus;
import vn.lotusviet.hotelmgmt.service.mail.MailService;
import vn.lotusviet.hotelmgmt.service.storage.StorageService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static vn.lotusviet.hotelmgmt.service.AccountService.AccountStatus.*;

@WebMvcTest(
    value = AccountController.class,
    excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class AccountControllerTest extends AbstractRestControllerTest {

  @Autowired private MockMvc mvc;

  @MockBean private AccountService accountService;

  @MockBean private AccountRepository accountRepository;

  @MockBean private MailService mailService;

  @MockBean private StorageService storageService;

  @MockBean private PasswordEncoder passwordEncoder;

  @Test
  void givenInvalidEmail_whenGetAccountStatusByEmail_thenGet400() throws Exception {
    String invalidEmail = "abc";
    given(accountService.getAccountStatusByEmail(invalidEmail)).willReturn(NOT_EXIST);

    var resultActions = whenRequestGetAccountStatusByEmail(invalidEmail);

    resultActions.andExpect(status().isBadRequest());
  }

  @Test
  void givenValidEmail_whenGetAccountStatusByEmail_thenGetCorrectStatus() throws Exception {
    String email = "abc@gmail.com";
    List<AccountStatus> statuses =
        Arrays.stream(values()).filter(s -> !s.equals(NOT_EXIST)).collect(Collectors.toList());
    for (AccountStatus status : statuses) {
      given(accountService.getAccountStatusByEmail(email)).willReturn(status);

      var resultAction = whenRequestGetAccountStatusByEmail(email);

      resultAction.andExpect(status().isOk()).andExpect(jsonPath("$.status").value(status.name()));
    }
  }

  @Test
  void givenNotExistEmail_whenSendPasswordResetMail_thenGetErrorResponse() throws Exception {
    String email = "notexist@mail.com";
    given(accountService.getAccountStatusByEmail(email)).willReturn(NOT_EXIST);

    var resultAction = mvc.perform(post(getUrl("/reset_password/init")).param("email", email));

    resultAction
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details.[0].issue").value("email.notExist"))
        .andExpect(jsonPath("$.details.[0].field").value("email"));
  }

  @Test
  void givenNotActivatedEmail_whenSendPasswordResetMail_thenGetErrorResponse() throws Exception {
    String email = "notActivated@mail.com";
    given(accountService.getAccountStatusByEmail(email)).willReturn(NOT_ACTIVATED);

    var resultAction = mvc.perform(post(getUrl("/reset_password/init")).param("email", email));

    resultAction
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details.[0].issue").value("email.notActivated"))
        .andExpect(jsonPath("$.details.[0].field").value("email"));
  }

  @Test
  void givenActivatedEmail_whenSendPasswordResetEmail_thenGetAcceptedResponse() throws Exception {
    // Given
    String email = "activated@mail.com";
    Account activatedAccount = new Account();

    given(accountService.getAccountStatusByEmail(email)).willReturn(ACTIVATED);
    given(accountService.getActivatedAccountByEmail(email))
        .willReturn(Optional.of(activatedAccount));

    // When
    var resultAction = mvc.perform(post(getUrl("/reset_password/init")).param("email", email));

    // Then
    verify(accountService).updateAccountPasswordResetKey(activatedAccount);
    verify(mailService).sendPasswordResetMail(activatedAccount);
    resultAction.andExpect(status().isAccepted());
  }

  @Test
  void givenValidResetKey_whenCompletePasswordReset_thenGetOKResponse() throws Exception {
    // Given
    KeyAndPasswordDto keyAndPassword =
        new KeyAndPasswordDto().setNewPassword("newPw").setKey("secret");
    Account account = new Account();
    given(accountService.getAccountByPasswordResetKey(keyAndPassword.getKey()))
        .willReturn(Optional.of(account));

    // When
    var resultAction =
        mvc.perform(
            post(getUrl("/reset_password/finish"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(keyAndPassword)));

    // Then
    verify(accountService).updateAccountPassword(account, keyAndPassword.getNewPassword());
    resultAction.andExpect(status().isOk());
  }

  @Test
  void givenInvalidResetKey_whenCompleteResetPassword_thenGet404Response() throws Exception {
    // Given
    KeyAndPasswordDto keyAndPassword =
        new KeyAndPasswordDto().setNewPassword("newPw").setKey("not found");
    given(accountService.getAccountByPasswordResetKey(keyAndPassword.getKey()))
        .willReturn(Optional.empty());

    // When
    var resultAction =
        mvc.perform(
            post(getUrl("/reset_password/finish"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(keyAndPassword)));

    // Then
    TestUtil.verifyErrorResponseWithFields(
        resultAction,
        HttpStatus.BAD_REQUEST,
        "INVALID_REQUEST",
        List.of(new ErrorDetail("resetKey.invalid")));
  }

  @Test
  @WithCustomerRoleMockUser
  void givenValidLangKey_whenUpdateLoginAccount_thenGetOKResponse() throws Exception {
    List<String> langkeys = List.of("vi", "en");
    for (String langkey : langkeys) {
      var resultAction = mvc.perform(put(getUrl("/update_langkey")).param("langKey", langkey));

      resultAction.andExpect(status().isOk());
    }
  }

  @Test
  @WithCustomerRoleMockUser
  void givenInValidLangKey_whenUpdateLoginAccount_thenGetOKResponse() throws Exception {
    String invalidLangKey = "ru";

    var resultAction = mvc.perform(put(getUrl("/update_langkey")).param("langKey", invalidLangKey));

    resultAction.andExpect(status().isBadRequest()).andDo(print());
  }

  @Test
  @WithCustomerRoleMockUser
  void givenNotMatchCurrentPassword_whenUpdateLoginAccountPassword_thenGet404Response()
      throws Exception {
    Account account = new Account();
    PasswordChangeDto passwordChange =
        new PasswordChangeDto()
            .setCurrentPassword("currentPassword")
            .setNewPassword("newValidPassword");

    given(accountService.getLoginAccount()).willReturn(account);
    given(passwordEncoder.matches(passwordChange.getCurrentPassword(), account.getHashedPassword()))
        .willReturn(false);

    var resultAction =
        mvc.perform(
            put(getUrl("/change_password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(passwordChange)));

    TestUtil.verifyErrorResponseWithFields(
        resultAction,
        HttpStatus.BAD_REQUEST,
        "INVALID_REQUEST",
        List.of(new ErrorDetail("currentPassword.notMatch")));
  }

  @Test
  @WithCustomerRoleMockUser
  void givenMatchCurrentPassword_whenUpdateLoginAccountPassword_thenGet404Response()
      throws Exception {
    Account account = new Account();
    PasswordChangeDto passwordChange =
        new PasswordChangeDto()
            .setCurrentPassword("currentPassword")
            .setNewPassword("newValidPassword");

    given(accountService.getLoginAccount()).willReturn(account);
    given(passwordEncoder.matches(passwordChange.getCurrentPassword(), account.getHashedPassword()))
        .willReturn(true);

    var resultAction =
        mvc.perform(
            put(getUrl("/change_password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(passwordChange)));

    resultAction.andExpect(status().isOk());
  }

  @Test
  @WithCustomerRoleMockUser
  void givenValidAccountProfile_whenUpdateLoginAccountProfile_thenGetOkResponse() throws Exception {
    // Given
    AccountProfileUpdateDto profileUpdate =
        new AccountProfileUpdateDto()
            .setFirstName("fname")
            .setLastName("lname")
            .setPhone("1234567891")
            .setAddress("address");
    Account account = new Account();

    given(accountService.getLoginAccount()).willReturn(account);

    // When
    var resultAction =
        mvc.perform(
            put(getUrl("/update_profile"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(TestUtil.convertObjectToJsonBytes(profileUpdate)));

    // Then
    // verify(accountService).updateAccountProfile(account, profileUpdate);
    resultAction.andExpect(status().isOk());
  }

  @Test
  @WithCustomerRoleMockUser
  void givenValidAccountImage_whenUpdateLoginAccountImage_thenGetOkResponse() throws Exception {
    // Given
    MockMultipartFile image =
        TestUtil.createMockMultipartFileFromByteArray(
            "image", "image.png", MediaType.APPLICATION_JSON_VALUE, "");
    Account account = new Account();
    given(accountService.getLoginAccount()).willReturn(account);

    // When
    var resultAction = mvc.perform(multipart(getUrl("/update_image")).file(image));

    // Then
    verify(accountService).updateAccountImage(account, image);
    resultAction.andExpect(status().isOk());
  }

  @Test
  void givenNotExistEmailOfCustomerRegisterData_whenRegisterNewAccount_thenGetOkResponse()
      throws Exception {
    // Given
    CustomerRegisterDto registerData =
        new CustomerRegisterDto()
            .setEmail("notExist@mail.com")
            .setFirstName("fname")
            .setLastName("lname")
            .setPassword("password")
            .setLangKey("vi");

    for (AccountStatus acceptedStatus : List.of(NOT_EXIST, NOT_ACTIVATED)) {
      Account account = new Account();

      given(accountService.getAccountStatusByEmail(registerData.getEmail()))
          .willReturn(acceptedStatus);
      given(accountService.saveCustomerAccountFromRegistration(registerData)).willReturn(account);

      // When
      var resultAction =
          mvc.perform(
              post(getUrl("/register"))
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(TestUtil.convertObjectToJsonBytes(registerData)));

      // Then
      // verify(mailService).sendActivationEmail(any());
      resultAction.andExpect(status().isCreated());
    }
  }

  @Test
  void givenExistEmailOfCustomerRegisterData_whenRegisterNewAccount_thenGetOkResponse()
      throws Exception {
    // Given
    CustomerRegisterDto registerData =
        new CustomerRegisterDto()
            .setEmail("existed@mail.com")
            .setFirstName("fname")
            .setLastName("lname")
            .setPassword("password")
            .setLangKey("vi");

    Account account = new Account();

    given(accountService.getAccountStatusByEmail(registerData.getEmail())).willReturn(ACTIVATED);
    given(accountService.saveCustomerAccountFromRegistration(registerData)).willReturn(account);

    // When
    var resultAction =
        mvc.perform(
            post(getUrl("/register"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(registerData)));

    // Then
    TestUtil.verifyErrorResponseWithFields(
        resultAction,
        HttpStatus.BAD_REQUEST,
        "INVALID_REQUEST",
        List.of(new ErrorDetail("customerRegister.email.exist")));
  }

  @Override
  protected String getUrlPrefix() {
    return AccountController.URL_PREFIX;
  }

  private ResultActions whenRequestGetAccountStatusByEmail(String email) throws Exception {
    return mvc.perform(
        get(getUrl("/account_status")).param("email", email).accept(MediaType.APPLICATION_JSON));
  }
}