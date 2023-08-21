package vn.lotusviet.hotelmgmt.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.exception.ServiceNameUniqueException;
import vn.lotusviet.hotelmgmt.exception.ServiceNotFoundException;
import vn.lotusviet.hotelmgmt.exception.ServicePriceUpdateNotAllowedException;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServicePriceUpdateDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceUpdateDto;
import vn.lotusviet.hotelmgmt.model.dto.tracking.ServicePriceHistoryDto;
import vn.lotusviet.hotelmgmt.model.entity.tracking.ServicePriceHistory;
import vn.lotusviet.hotelmgmt.repository.checkout.ServiceRepository;
import vn.lotusviet.hotelmgmt.repository.tracking.ServicePriceHistoryRepository;
import vn.lotusviet.hotelmgmt.service.CommonService;
import vn.lotusviet.hotelmgmt.service.SvService;
import vn.lotusviet.hotelmgmt.service.factory.ServiceFactory;
import vn.lotusviet.hotelmgmt.service.mapper.ServiceMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class SvServiceImpl implements SvService {

  private static final Logger log = LoggerFactory.getLogger(SvServiceImpl.class);

  private final ServiceRepository serviceRepository;
  private final ServicePriceHistoryRepository servicePriceHistoryRepository;
  private final ServiceMapper serviceMapper;
  private final ServiceFactory serviceFactory;
  private final CacheManager cacheManager;
  private final CommonService commonService;

  public SvServiceImpl(
      ServiceRepository serviceRepository,
      ServicePriceHistoryRepository servicePriceHistoryRepository,
      ServiceMapper serviceMapper,
      ServiceFactory serviceFactory,
      CacheManager cacheManager,
      CommonService commonService) {
    this.serviceRepository = serviceRepository;
    this.commonService = commonService;
    this.servicePriceHistoryRepository = servicePriceHistoryRepository;
    this.serviceMapper = serviceMapper;
    this.serviceFactory = serviceFactory;
    this.cacheManager = cacheManager;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ServiceDto> getAllServices() {
    return serviceMapper.toDto(serviceRepository.findAll());
  }

  @Override
  @LogAround(output = false)
  @Transactional(readOnly = true)
  public List<ServiceDto> getActiveServices() {
    return serviceMapper.toDto(serviceRepository.findAllByIsActiveIsTrue());
  }

  @Override
  @LogAround
  public ServiceDto saveNewService(final ServiceCreateDto serviceCreateDto) {
    Objects.requireNonNull(serviceCreateDto);

    if (serviceRepository.existsByName(serviceCreateDto.getName())) {
      throw new ServiceNameUniqueException();
    }

    vn.lotusviet.hotelmgmt.model.entity.service.Service service =
        serviceFactory.createService(serviceCreateDto);

    service = serviceRepository.save(service);
    if (service.getUnitPrice() != null) {
      saveNewServicePriceHistory(service.getId(), service.getUnitPrice(), service.getVat());
    }
    return serviceMapper.toDto(service);
  }

  @Override
  @Transactional(readOnly = true)
  public ServiceDto getDetailOfService(final int serviceId) {
    return serviceRepository
        .findById(serviceId)
        .map(serviceMapper::toDto)
        .orElseThrow(() -> new ServiceNotFoundException(serviceId));
  }

  @Override
  @LogAround
  public ServiceDto updateService(int serviceId, final ServiceUpdateDto serviceUpdateDto) {
    Objects.requireNonNull(serviceUpdateDto);

    final vn.lotusviet.hotelmgmt.model.entity.service.Service service =
        commonService.getServiceById(serviceId);

    if (!service.getName().equalsIgnoreCase(serviceUpdateDto.getName())
        && serviceRepository.existsByName(serviceUpdateDto.getName())) {
      throw new ServiceNameUniqueException();
    }

    service
        .setName(serviceUpdateDto.getName())
        .setActive(serviceUpdateDto.getActive())
        .setServiceType(commonService.getServiceTypeById(serviceUpdateDto.getServiceTypeId()));

    clearServiceCache(service);

    return serviceMapper.toDto(serviceRepository.save(service));
  }

  @Override
  @LogAround
  public ServiceDto updateServicePrice(
      final int serviceId, final ServicePriceUpdateDto servicePriceUpdateDto) {
    Objects.requireNonNull(servicePriceUpdateDto);

    final vn.lotusviet.hotelmgmt.model.entity.service.Service service =
        commonService.getServiceById(serviceId);

    boolean changed = false;

    if (servicePriceUpdateDto.getVat() != null
        && service.getVat() != servicePriceUpdateDto.getVat()) {
      service.setVat(servicePriceUpdateDto.getVat());
      changed = true;
    }

    if (servicePriceUpdateDto.getPrice() != null) {
      if (service.getUnitPrice() == null) {
        throw new ServicePriceUpdateNotAllowedException("Service not support update price");
      }
      service.setUnitPrice(servicePriceUpdateDto.getPrice());
      changed = true;
    }

    if (changed) {
      saveNewServicePriceHistory(serviceId, service.getUnitPrice(), service.getVat());
      clearServiceCache(service);
    }

    return serviceMapper.toDto(serviceRepository.save(service));
  }

  @Override
  @LogAround(output = false)
  @Transactional(readOnly = true)
  public Page<ServicePriceHistoryDto> getPriceHistoryOfService(
      final int serviceId, final Pageable pageable) {
    return serviceRepository
        .findById(serviceId)
        .map(
            service ->
                servicePriceHistoryRepository
                    .findByServiceId(service.getId(), pageable)
                    .map(serviceMapper::toPriceHistoryDto))
        .orElseThrow(() -> new ServiceNotFoundException(serviceId));
  }

  private ServicePriceHistoryDto saveNewServicePriceHistory(
      final int serviceId, final int price, final int vat) {
    final vn.lotusviet.hotelmgmt.model.entity.service.Service service =
        commonService.getServiceById(serviceId);

    if (servicePriceHistoryRepository.existsById(
        new ServicePriceHistory.ServicePriceHistoryId(service, LocalDate.now()))) {
      throw new ServicePriceUpdateNotAllowedException("Update allowance only one per day");
    }

    log.debug(
        "Save service price history [service id: '{}', new price: '{}', new vat: '{}']",
        service.getId(),
        price,
        vat);

    final ServicePriceHistory servicePriceHistory =
        serviceFactory.createServicePriceHistory(service, price, vat);
    return serviceMapper.toPriceHistoryDto(servicePriceHistoryRepository.save(servicePriceHistory));
  }

  private void clearServiceCache(
      final vn.lotusviet.hotelmgmt.model.entity.service.Service service) {
    log.debug("Clear service cache by id '{}'", service.getId());
    Objects.requireNonNull(
            cacheManager.getCache(vn.lotusviet.hotelmgmt.model.entity.service.Service.CACHE))
        .evict(service.getId());
  }
}