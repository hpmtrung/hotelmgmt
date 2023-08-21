package vn.lotusviet.hotelmgmt.repository.misc;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethod;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {

  String FIND_BY_CODE_CACHE_NAME = "findPaymentMethodByCode";

  @Cacheable(
      value = FIND_BY_CODE_CACHE_NAME,
      key = "#code.name()",
      condition = "#result != null && #result.isPresent()")
  Optional<PaymentMethod> findByCode(PaymentMethodCode code);
}