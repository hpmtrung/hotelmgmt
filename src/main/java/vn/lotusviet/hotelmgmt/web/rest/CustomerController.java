package vn.lotusviet.hotelmgmt.web.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.annotation.security.CustomerSecured;
import vn.lotusviet.hotelmgmt.core.annotation.security.PortalSecured;
import vn.lotusviet.hotelmgmt.core.annotation.validation.personalid.PersonalIdConstraint;
import vn.lotusviet.hotelmgmt.core.exception.InvalidParamException;
import vn.lotusviet.hotelmgmt.core.service.reader.FileReader.FileReaderException;
import vn.lotusviet.hotelmgmt.exception.ApplicationErrorCode;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.service.CustomerService;
import vn.lotusviet.hotelmgmt.service.ReservationService;
import vn.lotusviet.hotelmgmt.service.mapper.CustomerMapper;
import vn.lotusviet.hotelmgmt.service.mapper.ReservationMapper;
import vn.lotusviet.hotelmgmt.service.reader.CustomerImportStyleSheetReader;
import vn.lotusviet.hotelmgmt.util.FileUtil;
import vn.lotusviet.hotelmgmt.util.PaginationUtil;

import javax.validation.constraints.PositiveOrZero;
import java.io.IOException;
import java.util.List;

import static vn.lotusviet.hotelmgmt.web.rest.CustomerController.URL_PREFIX;

@RestController
@Validated
@RequestMapping(URL_PREFIX)
public class CustomerController {

  public static final String URL_PREFIX = "/api/v1/customer";

  private final CustomerService customerService;
  private final ReservationService reservationService;
  private final CustomerImportStyleSheetReader importStyleSheetReader;
  private final CustomerMapper customerMapper;
  private final ReservationMapper reservationMapper;

  public CustomerController(
      CustomerService customerService,
      ReservationService reservationService,
      CustomerImportStyleSheetReader importStyleSheetReader,
      CustomerMapper customerMapper,
      ReservationMapper reservationMapper) {
    this.customerService = customerService;
    this.reservationService = reservationService;
    this.importStyleSheetReader = importStyleSheetReader;
    this.customerMapper = customerMapper;
    this.reservationMapper = reservationMapper;
  }

  @PortalSecured
  @LogAround(output = false)
  @GetMapping
  public ResponseEntity<List<CustomerDto>> getAllCustomers(
      final @RequestParam String personalIdPrefix, final Pageable pageable) {
    Page<CustomerDto> page;
    if (StringUtils.hasText(personalIdPrefix)) {
      page = customerService.getCustomersByMatchingPersonalIdPrefix(personalIdPrefix, pageable);
    } else {
      page = customerService.getAllCustomers(pageable, customerMapper::toCustomerDto);
    }
    return PaginationUtil.createPaginationResponse(page);
  }

  @PortalSecured
  @GetMapping("/{personalId}")
  public CustomerDto getCustomerByPersonalId(
      final @PathVariable @PersonalIdConstraint String personalId) {
    return customerService.getCustomerByPersonalId(personalId, customerMapper::toCustomerDto);
  }

  @PortalSecured
  @PostMapping("/import/stylesheet")
  public void importCustomerByStyleSheetFile(
      final @RequestPart("file") MultipartFile multipartFile) {
    List<Customer> records;
    try {
      records =
          FileUtil.doTaskWithTemporaryFileFromMultipart(
              multipartFile, importStyleSheetReader::readFromFile);
      customerService.saveOccupiedCustomerIfNotExist(customerMapper.toCustomerDto(records));
    } catch (IOException e) {
      throw new FileReaderException(e);
    }
  }

  @PortalSecured
  @LogAround
  @PostMapping("/import/stylesheet/preview")
  public List<CustomerDto> getCustomerImportPreviewByStyleSheetFile(
      final @RequestPart("file") MultipartFile multipartFile) {
    List<Customer> records;
    try {
      records =
          FileUtil.doTaskWithTemporaryFileFromMultipart(
              multipartFile, importStyleSheetReader::readFromFile);
      return customerMapper.toCustomerDto(records);
    } catch (IOException e) {
      throw new FileReaderException(e);
    }
  }

  @PortalSecured
  @GetMapping("/search/by_personal_id")
  public ResponseEntity<List<CustomerDto>> getCustomersByMatchingPersonalIdPrefix(
      final @RequestParam(defaultValue = "") String personalIdPrefix, final Pageable pageable) {
    return PaginationUtil.createPaginationResponse(
        customerService.getCustomersByMatchingPersonalIdPrefix(personalIdPrefix, pageable));
  }

  @CustomerSecured
  @LogAround(jsonOutput = true)
  @GetMapping("/reservations/{reservationId}")
  public ReservationDto getDetailOfReservationsOfLoginAccount(
      final @PathVariable @PositiveOrZero long reservationId) {
    Customer customer = customerService.getLoginCustomer();

    boolean isReservationNotExist =
        !reservationService.isReservationExistByIdAndOwnerId(
            reservationId, customer.getPersonalId());

    if (isReservationNotExist) {
      throw InvalidParamException.builder()
          .path(
              ApplicationErrorCode.RESERVATION_ID_NOT_EXIST,
              "reservationId",
              String.valueOf(reservationId),
              "Reservation id is not found")
          .build();
    }

    return reservationService.getDetailOfReservation(
        reservationId, reservationMapper::toReservationDtoWithoutOwnerInfo);
  }

  @CustomerSecured
  @GetMapping("/reservations")
  public ResponseEntity<List<ReservationDto>> getLoginAccountReservations(final Pageable pageable) {
    Customer customer = customerService.getLoginCustomer();
    Page<ReservationDto> reservations =
        reservationService.getReservationsOfCustomer(
            customer.getPersonalId(),
            pageable,
            reservationMapper::toBasicReservationDtoWithoutOwnerInfo);
    return PaginationUtil.createPaginationResponse(reservations);
  }
}