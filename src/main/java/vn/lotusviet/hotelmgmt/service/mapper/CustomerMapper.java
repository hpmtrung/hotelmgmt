package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

  @Mapping(
      target = "email",
      source = "account.email",
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
  CustomerDto toCustomerDto(Customer entity);

  List<CustomerDto> toCustomerDto(Collection<Customer> entity);

  @Mapping(target = "account", ignore = true)
  Customer toCustomerEntity(CustomerDto dto);
}