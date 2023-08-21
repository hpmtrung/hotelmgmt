package vn.lotusviet.hotelmgmt.repository.checkout;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.rental.Invoice;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Long> {

  boolean existsByRentalIdAndDepositUsedIsTrue(long rentalId);

  boolean existsByRentalIdAndRentalDiscountUsedIsTrue(long rentalId);

  @Query("select sum(i.total) from Invoice i where i.rental.id = ?1")
  int getInvoiceTotalFromRentalId(long rentalId);

  List<Invoice> findByCreatedAtBetweenAndTaxCodeIsNull(LocalDateTime dateFrom, LocalDateTime dateTo);

  List<Invoice> findByCreatedAtBetweenAndTaxCodeIsNotNull(LocalDateTime dateFrom, LocalDateTime dateTo);
}