package vn.lotusviet.hotelmgmt.repository.person;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.person.Account;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AccountRepository
    extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

  String CACHE_ACCOUNT_ENTITY_BY_ID = "cacheAccountEntityById";
  String CACHE_ACCOUNT_ENTITY_BY_EMAIL = "cacheAccountEntityByEmail";

  @EntityGraph(attributePaths = "authority")
  Optional<Account> findById(long id);

  Optional<Account> findByActivationKey(String activationKey);

  Optional<Account> findByPwResetKeyAndActivatedIsTrue(String resetKey);

  @Cacheable(CACHE_ACCOUNT_ENTITY_BY_EMAIL)
  @EntityGraph(attributePaths = "authority")
  Optional<Account> findByEmail(String email);

  boolean existsByEmail(String email);
}