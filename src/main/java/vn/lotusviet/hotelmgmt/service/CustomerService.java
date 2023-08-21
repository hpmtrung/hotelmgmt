package vn.lotusviet.hotelmgmt.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface CustomerService {

  Page<CustomerDto> getAllCustomers(
      @NotNull Pageable pageable, @NotNull Function<Customer, CustomerDto> mappingFunction);

  Page<CustomerDto> getCustomersByMatchingPersonalIdPrefix(
      @NotNull String pattern, @NotNull Pageable pageable);

  CustomerDto getCustomerByPersonalId(
      @NonNull String personalId, @NotNull Function<Customer, CustomerDto> mappingFunction);

  Optional<Customer> getCustomerByAccountId(long accountId);

  Customer saveOccupiedCustomerIfNotExist(@NonNull CustomerDto dto);

  void saveOccupiedCustomerIfNotExist(@NonNull List<CustomerDto> customerDtos);

  /**
   * Save new or update customer. Save new account for customer if customer's email is not existed
   */
  Customer saveOrUpdateCustomer(@NonNull CustomerDto dto);

  Customer getLoginCustomer();

  void clearCustomerCache(Customer customer);
}