package vn.lotusviet.hotelmgmt.service.impl;

import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.exception.AuditedLoginNotFoundException;
import vn.lotusviet.hotelmgmt.exception.EmployeeNotFoundException;
import vn.lotusviet.hotelmgmt.exception.RequestParamInvalidException;
import vn.lotusviet.hotelmgmt.exception.entity.DepartmentNotFoundException;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeUpdateDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;
import vn.lotusviet.hotelmgmt.model.entity.person.Department;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;
import vn.lotusviet.hotelmgmt.repository.person.DepartmentRepository;
import vn.lotusviet.hotelmgmt.repository.person.EmployeeRepository;
import vn.lotusviet.hotelmgmt.service.AccountService;
import vn.lotusviet.hotelmgmt.service.EmployeeService;
import vn.lotusviet.hotelmgmt.service.mapper.EmployeeMapper;

import java.util.Objects;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final EmployeeMapper employeeMapper;
  private final AccountService accountService;
  private final DepartmentRepository departmentRepository;
  private final CacheManager cacheManager;

  public EmployeeServiceImpl(
      EmployeeRepository employeeRepository,
      EmployeeMapper employeeMapper,
      AccountService accountService,
      DepartmentRepository departmentRepository,
      CacheManager cacheManager) {
    this.employeeRepository = employeeRepository;
    this.employeeMapper = employeeMapper;
    this.accountService = accountService;
    this.departmentRepository = departmentRepository;
    this.cacheManager = cacheManager;
  }

  @Transactional(readOnly = true)
  public Department getDepartmentById(final int departmentId) {
    return departmentRepository
        .findById(departmentId)
        .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
  }

  @Transactional(readOnly = true)
  public Employee getEmployeeById(final int id) {
    return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
  }

  @Override
  @LogAround(output = false)
  @Transactional(readOnly = true)
  public Page<EmployeeDto> getAllEmployees(final Pageable pageable) {
    return employeeRepository.findAll(pageable).map(employeeMapper::toEmployeeDto);
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public EmployeeDto getDetailOfEmployee(final int id) {
    return employeeRepository
        .findById(id)
        .map(employeeMapper::toEmployeeDto)
        .orElseThrow(() -> new EmployeeNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public Employee getAuditedLogin() {
    Account account = accountService.getLoginAccount();
    return employeeRepository
        .findByAccount(account)
        .orElseThrow(AuditedLoginNotFoundException::new);
  }

  @Override
  @LogAround
  public EmployeeDto updateEmployee(
      final int employeeId, final EmployeeUpdateDto employeeUpdateDto) {
    Objects.requireNonNull(employeeUpdateDto);

    final Employee employee = getEmployeeById(employeeId);
    employeeMapper.partialUpdateEmployeeEntity(employee, employeeUpdateDto);
    if (employeeUpdateDto.getDepartmentId() != null) {
      employee.setDepartment(getDepartmentById(employeeUpdateDto.getDepartmentId()));
    }

    final Account employeeAccount = employee.getAccount();

    if (employeeUpdateDto.getAccountCreate() != null) {
      if (employeeAccount != null) {
        throw new RequestParamInvalidException(
            "Account creation is illegal", "accountCreate", "illegal");
      }
      employee.setAccount(
          accountService.saveEmployeeAccount(employee, employeeUpdateDto.getAccountCreate()));
    } else if (employeeUpdateDto.getAccountUpdate() != null) {
      if (employeeAccount == null) {
        throw new RequestParamInvalidException(
            "Employee has no account to update", "accountUpdate", "invalid");
      }

      accountService.updateEmployeeAccount(employee, employeeUpdateDto.getAccountUpdate());
    }
    clearEmployeeCaches();
    return employeeMapper.toEmployeeDto(employeeRepository.save(employee));
  }

  @Override
  @LogAround
  public EmployeeDto saveNewEmployee(final EmployeeCreateDto employeeCreateDto) {
    Objects.requireNonNull(employeeCreateDto);
    Employee newEmployee = employeeMapper.toEntity(employeeCreateDto);
    newEmployee.setDepartment(getDepartmentById(employeeCreateDto.getDepartmentId()));

    if (employeeCreateDto.getAccountCreate() != null) {
      newEmployee.setAccount(
          accountService.saveEmployeeAccount(newEmployee, employeeCreateDto.getAccountCreate()));
    }

    return employeeMapper.toEmployeeDto(employeeRepository.save(newEmployee));
  }

  private void clearEmployeeCaches() {
    Objects.requireNonNull(
            cacheManager.getCache(EmployeeRepository.FIND_EMPLOYEE_BY_ACCOUNT_CACHE_NAME))
        .clear();
  }
}