package vn.lotusviet.hotelmgmt.repository.tracking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.tracking.SuitePriceHistory;

@Repository
@Transactional(readOnly = true)
public interface SuitePriceHistoryRepository
    extends PagingAndSortingRepository<SuitePriceHistory, SuitePriceHistory.SuitePriceHistoryId> {

  @Query("select h from SuitePriceHistory h where h.id.suite.id = ?1")
  Page<SuitePriceHistory> findBySuiteId(int suiteId, Pageable pageable);
}