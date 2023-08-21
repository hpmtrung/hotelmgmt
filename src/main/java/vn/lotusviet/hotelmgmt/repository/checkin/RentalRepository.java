package vn.lotusviet.hotelmgmt.repository.checkin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface RentalRepository
    extends PagingAndSortingRepository<Rental, Long>, RentalCustomRepository {

  @Query("select r from Rental r where r.status.code <> 'FULL_CHECKOUT'")
  Page<Rental> findNotFullCheckoutCompleteRentals(Pageable pageable);

  @Query(
      "select r from Rental r join fetch RentalStatus s on r.status = s "
          + "where r.checkInAt >= :dateFrom and r.checkOutAt <= :dateTo "
          + "and s.code = 'FULL_CHECKOUT' and r.mergedInvoice is null "
          + "and not exists  (select 1 from Invoice iv where iv.rental = r and iv.taxCode is not null) "
          + "order by r.checkInAt asc, r.checkOutAt asc")
  List<Rental> findAllNotHasMergedInvoice(
      @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);
}