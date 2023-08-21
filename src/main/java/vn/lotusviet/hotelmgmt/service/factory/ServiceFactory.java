package vn.lotusviet.hotelmgmt.service.factory;

import org.jetbrains.annotations.NotNull;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceUsageCreateDto;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;
import vn.lotusviet.hotelmgmt.model.entity.service.Service;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceUsageDetail;
import vn.lotusviet.hotelmgmt.model.entity.tracking.ServicePriceHistory;

import java.util.List;

public interface ServiceFactory {

  Service createService(ServiceCreateDto serviceCreateDto);

  List<ServiceUsageDetail> createServiceUsageDetails(
      @NotNull RentalDetail rentalDetail,
      @NotNull List<ServiceUsageCreateDto.Item> serviceUsageCreateItems);

  ServicePriceHistory createServicePriceHistory(Service service, int newPrice, int newVat);
}