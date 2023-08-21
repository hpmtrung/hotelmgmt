package vn.lotusviet.hotelmgmt.service.impl;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.exception.InternalServerErrorException;
import vn.lotusviet.hotelmgmt.exception.entity.CustomerNotFoundException;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.repository.person.CustomerRepository;
import vn.lotusviet.hotelmgmt.service.AccountService;
import vn.lotusviet.hotelmgmt.service.CustomerService;
import vn.lotusviet.hotelmgmt.service.mapper.CustomerMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

  private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;
  private final AccountService accountService;
  private final CacheManager cacheManager;

  public CustomerServiceImpl(
      CustomerRepository customerRepository,
      CustomerMapper customerMapper,
      AccountService accountService,
      CacheManager cacheManager) {
    this.customerRepository = customerRepository;
    this.customerMapper = customerMapper;
    this.accountService = accountService;
    this.cacheManager = cacheManager;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CustomerDto> getAllCustomers(
      final @NotNull Pageable pageable,
      final @NotNull Function<Customer, CustomerDto> mappingFunction) {
    Objects.requireNonNull(pageable);
    Objects.requireNonNull(mappingFunction);
    return customerRepository.findAll(pageable).map(mappingFunction);
  }

  @Override
  @LogAround()
  @Transactional(readOnly = true)
  public Page<CustomerDto> getCustomersByMatchingPersonalIdPrefix(
      final @NotNull String pattern, final @NotNull Pageable pageable) {
    Objects.requireNonNull(pattern);
    Objects.requireNonNull(pageable);
    return customerRepository
        .findByPersonalIdStartingWith(pattern, pageable)
        .map(customerMapper::toCustomerDto);
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public CustomerDto getCustomerByPersonalId(
      final @NotNull String personalId,
      final @NotNull Function<Customer, CustomerDto> mappingFunction) {
    Objects.requireNonNull(personalId);
    Objects.requireNonNull(mappingFunction);

    return customerRepository
        .findById(personalId)
        .map(mappingFunction)
        .orElseThrow(() -> new CustomerNotFoundException(personalId));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Customer> getCustomerByAccountId(final long accountId) {
    return customerRepository.findByAccount_Id(accountId);
  }

  @Override
  @LogAround
  public Customer saveOccupiedCustomerIfNotExist(final @NotNull CustomerDto customerData) {
    Objects.requireNonNull(customerData);

    final String personalId = customerData.getPersonalId();
    Customer existCustomer = customerRepository.findById(personalId).orElse(null);
    if (existCustomer == null) {
      existCustomer = customerMapper.toCustomerEntity(customerData);
      existCustomer.setModifiedAt(LocalDateTime.now());
      existCustomer = customerRepository.save(existCustomer);
      log.debug("Save new occupied customer {}", existCustomer);
    } else {
      log.debug("No update exist customer {}", existCustomer);
    }
    return existCustomer;
  }

  @Override
  @LogAround
  public void saveOccupiedCustomerIfNotExist(final @NotNull List<CustomerDto> customerDtos) {
    Objects.requireNonNull(customerDtos);

    List<Customer> newCustomers = new ArrayList<>();
    for (final CustomerDto customerDto : customerDtos) {
      final String personalId = customerDto.getPersonalId();
      Customer customer = customerRepository.findById(personalId).orElse(null);
      if (customer == null) {
        customer = customerMapper.toCustomerEntity(customerDto);
        customer.setModifiedAt(LocalDateTime.now());
        newCustomers.add(customer);
      }
    }

    if (!newCustomers.isEmpty()) {
      customerRepository.saveAll(newCustomers);
    }
  }

  @Override
  @LogAround
  public Customer saveOrUpdateCustomer(final @NotNull CustomerDto customerData) {
    Objects.requireNonNull(customerData);
    final Customer customer = saveOccupiedCustomerIfNotExist(customerData);
    saveAccountForMainCustomer(customerData, customer);
    return customer;
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public Customer getLoginCustomer() {
    final Account account = accountService.getLoginAccount();
    return getCustomerByAccountId(account.getId())
        .orElseThrow(
            () ->
                new InternalServerErrorException(
                    String.format(
                        "Customer data has not been created for account [id: '%d', email: '%s']",
                        account.getId(), account.getEmail())));
  }

  @LogAround
  public void clearCustomerCache(final Customer customer) {
    Objects.requireNonNull(customer);
    Objects.requireNonNull(
            cacheManager.getCache(CustomerRepository.CACHE_CUSTOMER_ENTITY_BY_PERSONAL_ID))
        .evict(customer.getPersonalId());

    Account customerAccount = customer.getAccount();
    if (customerAccount != null) {
      Objects.requireNonNull(
              cacheManager.getCache(CustomerRepository.CACHE_CUSTOMER_ENTITY_BY_ACCOUNT_ID))
          .evict(customerAccount.getId());
    }
  }

  private void saveAccountForMainCustomer(final CustomerDto customerData, final Customer customer) {
    if (customer.getAccount() == null) {
      customer.setAccount(accountService.saveCustomerAccountIfNotExist(customerData));
      clearCustomerCache(customer);
    }
  }
}