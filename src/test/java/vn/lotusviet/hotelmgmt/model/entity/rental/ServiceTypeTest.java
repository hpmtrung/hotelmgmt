package vn.lotusviet.hotelmgmt.model.entity.rental;

import org.junit.jupiter.api.Test;
import vn.lotusviet.hotelmgmt.model.entity.service.Service;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceType;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTypeTest {

  private final ServiceType serviceType = new ServiceType();

  @Test
  void whenAddInvalidService_thenThrowException() {
    assertThrows(NullPointerException.class, () -> serviceType.addService(null));

    Service service = new Service();
    serviceType.addService(service);
    assertThrows(IllegalArgumentException.class, () -> serviceType.addService(service));
  }

  @Test
  void whenRemoveInvalidService_thenThrowException() {
    assertThrows(NullPointerException.class, () -> serviceType.removeService(null));

    Service service = new Service();
    assertThrows(IllegalArgumentException.class, () -> serviceType.removeService(service));
  }

  @Test
  void whenAddValidService_thenAssociationIsCorrect() {
    Service service = new Service();
    serviceType.addService(service);
    assertTrue(serviceType.getServices().contains(service));
    assertEquals(serviceType, service.getServiceType());
  }

  @Test
  void whenRemoveValidService_thenAssociationIsCorrect() {
    Service service = new Service();
    serviceType.addService(service);
    assertTrue(serviceType.getServices().contains(service));

    serviceType.removeService(service);
    assertFalse(serviceType.getServices().contains(service));
    assertNull(service.getServiceType());
  }

  @Test
  void whenRemoveAllService_thenAssociationIsCorrect() {
    Set<Service> services = Set.of(new Service().setName("1"), new Service().setName("2"));
    services.forEach(serviceType::addService);

    assertEquals(services.size(), serviceType.getServices().size());

    serviceType.removeAllService();
    assertTrue(serviceType.getServices().isEmpty());

    services.forEach(service -> assertNull(service.getServiceType()));
  }
}