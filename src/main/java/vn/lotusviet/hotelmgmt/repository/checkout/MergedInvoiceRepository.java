package vn.lotusviet.hotelmgmt.repository.checkout;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.rental.MergedInvoice;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface MergedInvoiceRepository extends PagingAndSortingRepository<MergedInvoice, Long> {

  List<MergedInvoice> findByCreatedAtBetween(LocalDateTime dateFrom, LocalDateTime dateTo);
}