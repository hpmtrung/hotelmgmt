package vn.lotusviet.hotelmgmt.repository.person;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.person.Authority;
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

  String FIND_AUTHORITY_BY_NAME = "findAuthorityByNameCache";

  @Cacheable(value = FIND_AUTHORITY_BY_NAME, key = "#name.name()")
  Optional<Authority> findByName(AuthorityName name);
}