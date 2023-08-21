package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceServiceUsageDetailDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceTypeDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceUsageDetailDto;
import vn.lotusviet.hotelmgmt.model.dto.tracking.ServicePriceHistoryDto;
import vn.lotusviet.hotelmgmt.model.entity.service.Service;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceType;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceUsageDetail;
import vn.lotusviet.hotelmgmt.model.entity.tracking.ServicePriceHistory;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {EmployeeMapper.class})
public interface ServiceMapper {

  @Mapping(target = "serviceTypeId", source = "serviceType.id")
  ServiceDto toDto(Service service);

  List<ServiceDto> toDto(List<Service> services);

  ServiceTypeDto toTypeDto(ServiceType serviceType);

  List<ServiceTypeDto> toTypeDto(List<ServiceType> serviceTypes);

  @Mapping(target = "rentalDetailId", source = "entity.rentalDetail.id")
  @Mapping(target = "service", source = "entity", qualifiedByName = "toUsageDetailDto_service")
  @Mapping(
      target = "createdBy",
      source = "entity.createdBy",
      qualifiedByName = EmployeeMapper.TO_DTO_WITH_ID_AND_NAME_QUALIFIER)
  ServiceUsageDetailDto toUsageDetailDto(ServiceUsageDetail entity);

  List<ServiceUsageDetailDto> toUsageDetailDto(List<ServiceUsageDetail> details);

  @Mapping(target = "serviceName", source = "service.name")
  @Mapping(target = "unitPrice", source = "servicePrice")
  InvoiceServiceUsageDetailDto toInvoiceServiceUsageDetailDto(ServiceUsageDetail entity);

  List<InvoiceServiceUsageDetailDto> toInvoiceServiceUsageDetailDto(
      List<ServiceUsageDetail> entities);

  @Mapping(target = "service", ignore = true)
  ServicePriceHistoryDto toPriceHistoryDto(ServicePriceHistory entity);

  @Named("toUsageDetailDto_service")
  default ServiceDto toUsageDetailDto_service(final ServiceUsageDetail entity) {
    return toDto(entity.getService()).setUnitPrice(entity.getServicePrice());
  }
}