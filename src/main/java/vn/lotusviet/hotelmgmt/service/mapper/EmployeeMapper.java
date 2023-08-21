package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.*;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeUpdateDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;

@Mapper(
    componentModel = "spring",
    uses = {DepartmentMapper.class})
public interface EmployeeMapper {

  String TO_DTO_WITH_ID_AND_NAME_QUALIFIER = "toEmployeeDtoWithIdAndName";

  @Mapping(target = "departmentId", source = "department.id")
  @Mapping(target = "account.authority", source = "account.authority.name")
  @Mapping(target = "account.email", source = "account.email")
  EmployeeDto toEmployeeDto(Employee entity);

  @Named(TO_DTO_WITH_ID_AND_NAME_QUALIFIER)
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  @Mapping(target = "firstName", source = "firstName")
  @Mapping(target = "lastName", source = "lastName")
  EmployeeDto toEmployeeDtoWithIdAndName(Employee entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "account", ignore = true)
  @Mapping(target = "department", ignore = true)
  void partialUpdateEmployeeEntity(@MappingTarget Employee entity, EmployeeUpdateDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "account", ignore = true)
  @Mapping(target = "department", ignore = true)
  Employee toEntity(EmployeeCreateDto dto);
}