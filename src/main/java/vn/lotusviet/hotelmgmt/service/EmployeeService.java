package vn.lotusviet.hotelmgmt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeUpdateDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;

public interface EmployeeService {

  Page<EmployeeDto> getAllEmployees(Pageable pageable);

  EmployeeDto getDetailOfEmployee(int id);

  EmployeeDto updateEmployee(int employeeId, EmployeeUpdateDto dto);

  EmployeeDto saveNewEmployee(EmployeeCreateDto dto);

  Employee getAuditedLogin();

}