package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.Mapper;
import vn.lotusviet.hotelmgmt.model.dto.person.DepartmentDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Department;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

  DepartmentDto toDepartmentDto(Department entity);

  List<DepartmentDto> toDepartmentDto(List<Department> entity);
}