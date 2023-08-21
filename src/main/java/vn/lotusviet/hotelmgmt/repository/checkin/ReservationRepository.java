package vn.lotusviet.hotelmgmt.repository.checkin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.reservation.Reservation;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ReservationRepository
    extends PagingAndSortingRepository<Reservation, Long>,
        ReservationCustomRepository,
        JpaSpecificationExecutor<Reservation> {

  @EntityGraph(Reservation.RESERVATION_DETAILS_FETCH_GRAPH)
  Optional<Reservation> findById(long id);

  boolean existsByIdAndOwner_PersonalId(long id, String personalId);

  Page<Reservation> findByOwner_PersonalId(String personalId, Pageable pageable);
}