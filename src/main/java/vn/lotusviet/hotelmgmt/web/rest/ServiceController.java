package vn.lotusviet.hotelmgmt.web.rest;

import com.vladmihalcea.concurrent.Retry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.lotusviet.hotelmgmt.core.annotation.security.AdminSecured;
import vn.lotusviet.hotelmgmt.core.annotation.security.PortalSecured;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServicePriceUpdateDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceUpdateDto;
import vn.lotusviet.hotelmgmt.model.dto.tracking.ServicePriceHistoryDto;
import vn.lotusviet.hotelmgmt.service.SvService;
import vn.lotusviet.hotelmgmt.util.PaginationUtil;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/service")
public class ServiceController {

  private final SvService svService;

  public ServiceController(SvService svService) {
    this.svService = svService;
  }

  @PortalSecured
  @GetMapping
  public List<ServiceDto> getAllServices() {
    return svService.getAllServices();
  }

  @PortalSecured
  @GetMapping("/active")
  public List<ServiceDto> getActiveServices() {
    return svService.getActiveServices();
  }

  @AdminSecured
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/save")
  public ServiceDto saveNewService(final @Valid @RequestBody ServiceCreateDto serviceCreateDto) {
    return svService.saveNewService(serviceCreateDto);
  }

  @PortalSecured
  @GetMapping("/{id}")
  public ServiceDto getDetailOfService(final @PathVariable int id) {
    return svService.getDetailOfService(id);
  }

  @AdminSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/{id}/update")
  public ServiceDto updateServiceInfo(
      final @PathVariable int id, final @Valid @RequestBody ServiceUpdateDto serviceUpdateDto) {
    return svService.updateService(id, serviceUpdateDto);
  }

  @AdminSecured
  @GetMapping("/{id}/price_history")
  public ResponseEntity<List<ServicePriceHistoryDto>> getPriceHistoryOfService(
      final @PathVariable int id, final Pageable pageable) {
    final Page<ServicePriceHistoryDto> page = svService.getPriceHistoryOfService(id, pageable);
    return PaginationUtil.createPaginationResponse(page);
  }

  @AdminSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/{id}/update_price")
  public ServiceDto updateServicePrice(
      final @PathVariable int id,
      final @Valid @RequestBody ServicePriceUpdateDto servicePriceUpdateDto) {
    return svService.updateServicePrice(id, servicePriceUpdateDto);
  }
}