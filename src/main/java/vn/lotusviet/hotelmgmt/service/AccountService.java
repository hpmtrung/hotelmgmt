package vn.lotusviet.hotelmgmt.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;
import vn.lotusviet.hotelmgmt.model.dto.person.*;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;
import vn.lotusviet.hotelmgmt.model.entity.person.Authority;
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;

import java.util.Optional;

public interface AccountService {

  AccountStatus getAccountStatusByEmail(String email);

  Authority getAuthorityByName(AuthorityName name);

  Optional<Account> getAccountByPasswordResetKey(@NonNull String key);

  void updateAccountPassword(@NonNull Account account, @NonNull String newPassword);

  Optional<Account> getActivatedAccountByEmail(@NonNull String email);

  void updateAccountPasswordResetKey(Account account);

  void updateAccountLangKey(Account account, String newLangKey);

  void updateAccountImage(Account account, MultipartFile imageFile);

  void updateAccountProfile(Account account, AccountProfileUpdateDto dto);

  Account getLoginAccount();

  Optional<AccountDto> getAccountById(long id);

  Optional<Account> activateAccountByActivationKey(String key);

  Account saveCustomerAccountFromRegistration(CustomerRegisterDto registerDto);

  Account saveCustomerAccountIfNotExist(CustomerDto dto);

  Optional<Account> updateCustomerAccount(CustomerDto customerDto);

  Account saveEmployeeAccount(
      @NotNull Employee employee, @NotNull EmployeeAccountCreateDto accountCreate);

  void updateEmployeeAccount(Employee employee, EmployeeAccountUpdateDto accountUpdate);

  enum AccountStatus {
    NOT_EXIST,
    NOT_ACTIVATED,
    ACTIVATED;
  }
}