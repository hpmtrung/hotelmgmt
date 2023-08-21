package vn.lotusviet.hotelmgmt.service.factory.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import vn.lotusviet.hotelmgmt.core.exception.ConstraintViolationException;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceUsageCreateDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;
import vn.lotusviet.hotelmgmt.model.entity.service.Service;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceUsageDetail;
import vn.lotusviet.hotelmgmt.model.entity.tracking.ServicePriceHistory;
import vn.lotusviet.hotelmgmt.service.CommonService;
import vn.lotusviet.hotelmgmt.service.EmployeeService;
import vn.lotusviet.hotelmgmt.service.factory.ServiceFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public final class DefaultServiceFactory implements ServiceFactory {

  private final EmployeeService employeeService;
  private final CommonService commonService;

  public DefaultServiceFactory(EmployeeService employeeService, CommonService commonService) {
    this.employeeService = employeeService;
    this.commonService = commonService;
  }

  @Override
  public Service createService(final ServiceCreateDto serviceCreateDto) {
    Objects.requireNonNull(serviceCreateDto);
    return new Service()
        .setName(serviceCreateDto.getName())
        .setIsActive(serviceCreateDto.getIsActive())
        .setUnitPrice(serviceCreateDto.getUnitPrice())
        .setVat(serviceCreateDto.getVat())
        .setServiceType(commonService.getServiceTypeById(serviceCreateDto.getServiceTypeId()));
  }

  @Override
  public List<ServiceUsageDetail> createServiceUsageDetails(
      @NotNull final RentalDetail rentalDetail,
      @NotNull final List<ServiceUsageCreateDto.Item> serviceUsageCreateItems) {
    Objects.requireNonNull(rentalDetail);
    Objects.requireNonNull(serviceUsageCreateItems);

    final List<ServiceUsageDetail> createdDetails = new ArrayList<>();

    final LocalDateTime createdAt = LocalDateTime.now();
    final Employee createdBy = employeeService.getAuditedLogin();

    for (final ServiceUsageCreateDto.Item item : serviceUsageCreateItems) {

      final vn.lotusviet.hotelmgmt.model.entity.service.Service service =
          commonService.getServiceById(item.getServiceId());

      verifyItemUnitPriceIsValid(item, service);

      final int quantity = item.getQuantity();
      final int unitPrice = item.getUnitPrice();
      final int total = quantity * unitPrice;
      final boolean isPaid = item.getIsPaid();

      createdDetails.add(
          new ServiceUsageDetail()
              .setRentalDetail(rentalDetail)
              .setService(service)
              .setCreatedBy(createdBy)
              .setCreatedAt(createdAt)
              .setQuantity(quantity)
              .setServicePrice(unitPrice)
              .setVat(service.getVat())
              .setTotal(total)
              .setIsPaid(isPaid));
    }

    return createdDetails;
  }

  @Override
  public ServicePriceHistory createServicePriceHistory(Service service, int newPrice, int newVat) {
    return new ServicePriceHistory()
        .setEditedAt(LocalDate.now())
        .setEditedBy(employeeService.getAuditedLogin())
        .setPrice(newPrice)
        .setVat(newVat)
        .setService(service);
  }

  private void verifyItemUnitPriceIsValid(final ServiceUsageCreateDto.Item item, final Service sv) {
    if (sv.getUnitPrice() != null && sv.getUnitPrice() > item.getUnitPrice()) {
      throw new ConstraintViolationException("service", "unitPrice", "invalid");
    }
  }
}