package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.*;
import vn.lotusviet.hotelmgmt.model.dto.person.AccountDto;
import vn.lotusviet.hotelmgmt.model.dto.person.AccountProfileUpdateDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

  @Mapping(target = "authority", source = "authority.name")
  AccountDto toDto(Account entity);

  List<AccountDto> toDto(List<Account> entityList);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "email", ignore = true)
  @Mapping(target = "hashedPassword", ignore = true)
  @Mapping(target = "activated", ignore = true)
  @Mapping(target = "activationKey", ignore = true)
  @Mapping(target = "pwResetKey", ignore = true)
  @Mapping(target = "resetAt", ignore = true)
  @Mapping(target = "langKey", ignore = true)
  @Mapping(target = "imageURL", ignore = true)
  @Mapping(target = "authority", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void partialUpdateAccountEntity(@MappingTarget Account entity, AccountProfileUpdateDto dto);
}