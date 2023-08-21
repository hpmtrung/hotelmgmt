package vn.lotusviet.hotelmgmt.repository.person;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CustomerRepository
    extends PagingAndSortingRepository<Customer, String>, JpaSpecificationExecutor<Customer> {

  String CACHE_CUSTOMER_ENTITY_BY_PERSONAL_ID = "cacheCustomerEntityByPersonalId";
  String CACHE_CUSTOMER_ENTITY_BY_ACCOUNT_ID = "cacheCustomerEntityByAccountId";

  @Cacheable(CACHE_CUSTOMER_ENTITY_BY_ACCOUNT_ID)
  Optional<Customer> findByAccount_Id(long accountId);

  @Query(
      value = "from Customer c left join fetch c.account where c.personalId like ?1%",
      countQuery =
          "select count(c) from Customer c left join c.account where c.personalId like ?1%")
  Page<Customer> findByPersonalIdStartingWith(String pattern, Pageable pageable);
}