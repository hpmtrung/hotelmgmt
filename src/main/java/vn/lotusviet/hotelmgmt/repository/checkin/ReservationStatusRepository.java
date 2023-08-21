package vn.lotusviet.hotelmgmt.repository.checkin;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatus;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatusCode;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ReservationStatusRepository extends JpaRepository<ReservationStatus, Integer> {

  String FIND_BY_CODE_CACHE_NAME = "findReservationStatusByCodeCache";

  @Cacheable(
      value = FIND_BY_CODE_CACHE_NAME,
      key = "#code.name()",
      condition = "#result != null && #result.isPresent()")
  Optional<ReservationStatus> findByCode(ReservationStatusCode code);
}