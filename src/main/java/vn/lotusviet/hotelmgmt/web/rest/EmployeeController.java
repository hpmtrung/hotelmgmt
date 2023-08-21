package vn.lotusviet.hotelmgmt.web.rest;

import com.vladmihalcea.concurrent.Retry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.lotusviet.hotelmgmt.core.annotation.security.AdminSecured;
import vn.lotusviet.hotelmgmt.exception.RequestParamInvalidException;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeUpdateDto;
import vn.lotusviet.hotelmgmt.service.EmployeeService;
import vn.lotusviet.hotelmgmt.util.PaginationUtil;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.util.List;

@AdminSecured
@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @GetMapping
  public ResponseEntity<List<EmployeeDto>> getAllEmployees(final Pageable pageable) {
    Page<EmployeeDto> page = employeeService.getAllEmployees(pageable);
    return PaginationUtil.createPaginationResponse(page);
  }

  @GetMapping("/{id}")
  public EmployeeDto getDetailOfEmployee(final @PathVariable int id) {
    return employeeService.getDetailOfEmployee(id);
  }

  @Retry(on = OptimisticLockException.class)
  @PutMapping("/{id}/update")
  public EmployeeDto saveNewEmployee(
      final @Valid @RequestBody EmployeeUpdateDto employeeUpdateDto, @PathVariable int id) {
    if (employeeUpdateDto.getAccountCreate() != null
        && employeeUpdateDto.getAccountUpdate() != null) {
      throw new RequestParamInvalidException(
          "Create or update employee " + "account only", "account", "invalid");
    }
    return employeeService.updateEmployee(id, employeeUpdateDto);
  }

  @PostMapping("/save")
  @ResponseStatus(HttpStatus.CREATED)
  public EmployeeDto saveNewEmployee(
      final @Valid @RequestBody EmployeeCreateDto employeeCreateDto) {
    return employeeService.saveNewEmployee(employeeCreateDto);
  }
}