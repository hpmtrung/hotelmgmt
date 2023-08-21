package vn.lotusviet.hotelmgmt.service.factory.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.lotusviet.hotelmgmt.exception.entity.AuthorityNotFoundException;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeAccountCreateDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;
import vn.lotusviet.hotelmgmt.model.entity.person.Authority;
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;
import vn.lotusviet.hotelmgmt.repository.person.AuthorityRepository;
import vn.lotusviet.hotelmgmt.service.SystemService;
import vn.lotusviet.hotelmgmt.service.factory.AccountFactory;
import vn.lotusviet.hotelmgmt.util.SecurityUtil.Random;

import java.time.LocalDateTime;
import java.util.Objects;

import static vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleKey.*;

@Component
public final class DefaultAccountFactory implements AccountFactory {

  private final PasswordEncoder passwordEncoder;
  private final SystemService systemService;
  private final AuthorityRepository authorityRepository;

  public DefaultAccountFactory(
      PasswordEncoder passwordEncoder,
      SystemService systemService,
      AuthorityRepository authorityRepository) {
    this.passwordEncoder = passwordEncoder;
    this.systemService = systemService;
    this.authorityRepository = authorityRepository;
  }

  @Override
  public Account createAccountForCustomer(CustomerDto customer) {
    return new Account()
        .setEmail(customer.getEmail())
        .setHashedPassword(passwordEncoder.encode(generateRegisterPassword()))
        .setFirstName(customer.getFirstName())
        .setLastName(customer.getLastName())
        .setAddress(customer.getAddress())
        .setPwResetKey(Random.generateResetKey())
        .setResetAt(LocalDateTime.now())
        .setPhone(customer.getPhone())
        .setActivated(true)
        .setLangKey(
            systemService.getBusinessRuleValue(
                CUSTOMER_ACCOUNT_REGISTRATION_DEFAULT_LANGKEY, String.class))
        .setAuthority(getAuthorityByName(AuthorityName.ROLE_CUSTOMER));
  }

  @Override
  public Account createAccountForEmployee(
      @NotNull EmployeeAccountCreateDto accountCreate, @NotNull Employee employee) {
    Objects.requireNonNull(accountCreate);

    return new Account()
        .setEmail(accountCreate.getEmail())
        .setHashedPassword(passwordEncoder.encode(generateRegisterPassword()))
        .setFirstName(employee.getFirstName())
        .setLastName(employee.getLastName())
        .setAddress(employee.getAddress())
        .setPwResetKey(Random.generateResetKey())
        .setResetAt(LocalDateTime.now())
        .setPhone(employee.getPhone())
        .setActivated(true)
        .setLangKey(
            systemService.getBusinessRuleValue(
                EMPLOYEE_ACCOUNT_CREATE_DEFAULT_LANGKEY, String.class))
        .setAuthority(getAuthorityByName(accountCreate.getAuthority()));
  }

  private Authority getAuthorityByName(AuthorityName name) {
    return authorityRepository
        .findByName(name)
        .orElseThrow(() -> new AuthorityNotFoundException(name));
  }

  private String generateRegisterPassword() {
    return Random.generateRandomAlphanumericString(
        systemService.getBusinessRuleValue(CUSTOMER_ACCOUNT_GENERATED_PW_LEN, Integer.class));
  }
}