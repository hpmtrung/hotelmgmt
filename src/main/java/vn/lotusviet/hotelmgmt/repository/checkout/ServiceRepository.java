package vn.lotusviet.hotelmgmt.repository.checkout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.entity.service.Service;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceType;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ServiceRepository
    extends JpaRepository<Service, Integer>, JpaSpecificationExecutor<Service> {

  List<Service> findByServiceType(ServiceType serviceType);

  List<Service> findAllByIsActiveIsTrue();

  boolean existsByName(String name);
}