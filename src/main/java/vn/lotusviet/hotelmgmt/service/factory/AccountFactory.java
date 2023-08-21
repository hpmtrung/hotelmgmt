package vn.lotusviet.hotelmgmt.service.factory;

import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeAccountCreateDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;

public interface AccountFactory {

  Account createAccountForCustomer(CustomerDto customer);

  Account createAccountForEmployee(EmployeeAccountCreateDto accountCreate, Employee employee);
}