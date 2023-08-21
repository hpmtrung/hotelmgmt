package vn.lotusviet.hotelmgmt.repository.misc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.system.BusinessRule;

@Repository
@Transactional(readOnly = true)
public interface BusinessRuleRepository extends JpaRepository<BusinessRule, Integer> {

  String FIND_BY_KEY_CACHE_NAME = "findBusinessRuleByKeyCache";
}