package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleDto;
import vn.lotusviet.hotelmgmt.model.entity.system.BusinessRule;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {EmployeeMapper.class})
public interface BusinessRuleMapper {

  @Mapping(
      target = "editedBy",
      source = "editedBy",
      qualifiedByName = EmployeeMapper.TO_DTO_WITH_ID_AND_NAME_QUALIFIER)
  BusinessRuleDto toDto(BusinessRule entity);

  List<BusinessRuleDto> toDto(List<BusinessRule> entities);
}