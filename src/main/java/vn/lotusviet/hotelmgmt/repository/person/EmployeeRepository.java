package vn.lotusviet.hotelmgmt.repository.person;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface EmployeeRepository
    extends PagingAndSortingRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

  String FIND_EMPLOYEE_BY_ACCOUNT_CACHE_NAME = "findEmployeeByAccountCache";

  @Cacheable(
      value = FIND_EMPLOYEE_BY_ACCOUNT_CACHE_NAME,
      key = "#account.id",
      condition = "#result != null && #result.isPresent()")
  Optional<Employee> findByAccount(Account account);
}