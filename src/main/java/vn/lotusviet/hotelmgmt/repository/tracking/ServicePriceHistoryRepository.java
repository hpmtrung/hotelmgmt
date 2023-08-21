package vn.lotusviet.hotelmgmt.repository.tracking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.tracking.ServicePriceHistory;

@Repository
@Transactional(readOnly = true)
public interface ServicePriceHistoryRepository
    extends PagingAndSortingRepository<
        ServicePriceHistory, ServicePriceHistory.ServicePriceHistoryId> {

  @Query("select h from ServicePriceHistory h where h.id.service.id = ?1")
  Page<ServicePriceHistory> findByServiceId(int serviceId, Pageable pageable);
}