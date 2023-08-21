package vn.lotusviet.hotelmgmt.repository.checkout;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatus;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatusCode;

import java.util.Optional;

@Repository
public interface RentalStatusRepository extends JpaRepository<RentalStatus, Integer> {

  String FIND_BY_CODE_CACHE_NAME = "findRentalStatusByCode";

  @Cacheable(value = FIND_BY_CODE_CACHE_NAME, key = "#code.name()")
  Optional<RentalStatus> findByCode(RentalStatusCode code);
}