package vn.lotusviet.hotelmgmt.repository.checkout;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.service.Service;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceUsageDetail;

@Repository
@Transactional(readOnly = true)
public interface ServiceUsageDetailRepository
    extends PagingAndSortingRepository<ServiceUsageDetail, Long> {

  boolean existsByIdAndIsPaidIsFalse(long id);

  boolean existsByService(Service service);
}