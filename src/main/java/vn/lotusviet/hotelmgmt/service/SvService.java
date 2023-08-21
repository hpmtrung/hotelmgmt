package vn.lotusviet.hotelmgmt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServicePriceUpdateDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceUpdateDto;
import vn.lotusviet.hotelmgmt.model.dto.tracking.ServicePriceHistoryDto;

import java.util.List;

public interface SvService {

  List<ServiceDto> getAllServices();

  List<ServiceDto> getActiveServices();

  ServiceDto saveNewService(ServiceCreateDto serviceCreateDto);

  ServiceDto getDetailOfService(int serviceId);

  ServiceDto updateService(int serviceId, ServiceUpdateDto serviceUpdateDto);

  ServiceDto updateServicePrice(int serviceId, ServicePriceUpdateDto servicePriceUpdateDto);

  Page<ServicePriceHistoryDto> getPriceHistoryOfService(int serviceId, Pageable pageable);
}