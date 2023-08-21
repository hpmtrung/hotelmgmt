package vn.lotusviet.hotelmgmt.web.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.annotation.security.CustomerSecured;
import vn.lotusviet.hotelmgmt.core.annotation.security.PortalSecured;
import vn.lotusviet.hotelmgmt.core.exception.InvalidParamException;
import vn.lotusviet.hotelmgmt.core.exception.PaypalRestException;
import vn.lotusviet.hotelmgmt.core.paying.PayingServiceFactory;
import vn.lotusviet.hotelmgmt.core.paying.PayingServiceFactory.ServiceParty;
import vn.lotusviet.hotelmgmt.core.paying.PaymentResponse;
import vn.lotusviet.hotelmgmt.exception.CustomerRentalDuplicatedException;
import vn.lotusviet.hotelmgmt.exception.RequestParamInvalidException;
import vn.lotusviet.hotelmgmt.exception.SuiteNotFoundException;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalDto;
import vn.lotusviet.hotelmgmt.model.dto.reservation.*;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteReservationDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteSearchRequestDto;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;
import vn.lotusviet.hotelmgmt.service.ReservationService;
import vn.lotusviet.hotelmgmt.service.RoomService;
import vn.lotusviet.hotelmgmt.service.mapper.ReservationMapper;
import vn.lotusviet.hotelmgmt.service.paying.PayingService;
import vn.lotusviet.hotelmgmt.util.PaginationUtil;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static vn.lotusviet.hotelmgmt.core.paying.PayingServiceFactory.ServiceParty.PAYPAL;
import static vn.lotusviet.hotelmgmt.core.paying.PayingServiceFactory.ServiceParty.fromString;
import static vn.lotusviet.hotelmgmt.web.rest.ReservationController.URL_PREFIX;

@RestController
@Validated
@RequestMapping(URL_PREFIX)
public class ReservationController {

  public static final String URL_PREFIX = "/api/v1/reservation";

  private final ReservationService reservationService;
  private final PayingServiceFactory payingServiceFactory;
  private final RoomService roomService;
  private final ReservationMapper reservationMapper;
  private PayingService payingService;

  public ReservationController(
      ReservationService reservationService,
      PayingServiceFactory payingServiceFactory,
      RoomService roomService,
      ReservationMapper reservationMapper) {
    this.reservationService = reservationService;
    this.payingServiceFactory = payingServiceFactory;
    this.payingService = payingServiceFactory.getDefaultPayingService();
    this.roomService = roomService;
    this.reservationMapper = reservationMapper;
  }

  @LogAround
  @GetMapping("/search")
  public ReservationSearchResponseDto findReservableSuites(
      final @RequestParam @Future LocalDate checkInAt,
      final @RequestParam @Future LocalDate checkOutAt,
      final @RequestParam @NotBlank String occupations) {
    verifyDateCheck(checkInAt, checkOutAt);
    final List<Integer> occupationArray =
        Arrays.stream(occupations.split("_")).map(Integer::parseInt).collect(Collectors.toList());
    if (occupationArray.isEmpty()) {
      throw new RequestParamInvalidException(
          "Occupation string is invalid", "occupations", "invalid");
    }
    return reservationService.getReservableSuites(checkInAt, checkOutAt, occupationArray);
  }

  @GetMapping("search_available_suites/by_date")
  public List<SuiteReservationDto> findAvailableSuitesByDate(
      final @RequestParam LocalDate checkInAt,
      final @RequestParam LocalDate checkOutAt,
      final @Valid SuiteSearchRequestDto filters,
      final Sort sort) {
    verifyDateCheck(checkInAt, checkOutAt);
    return reservationService.getAvailableSuitesByDate(checkInAt, checkOutAt, filters, sort);
  }

  @PostMapping("/checkout/init")
  public ReservationDto checkoutInitReservation(
      final @Valid @RequestBody ReservationCheckOutInitRequestDto
              reservationCheckOutInitRequestDto) {
    verifyDateCheck(
        reservationCheckOutInitRequestDto.getCheckInAt(),
        reservationCheckOutInitRequestDto.getCheckOutAt());

    final Map<Integer, Integer> reservedSuites =
        reservationCheckOutInitRequestDto.getSuiteIdMapping();

    reservedSuites.forEach(
        (suiteId, roomNum) -> {
          if (!roomService.isSuiteIdExist(suiteId)) {
            throw new SuiteNotFoundException(suiteId);
          }
        });

    return reservationService.saveNewTemporaryReservation(reservationCheckOutInitRequestDto);
  }

  @GetMapping("/checkout/init/detail/{id}")
  public ReservationDto refetchReservationCheckOut(final @PathVariable @PositiveOrZero long id) {
    return reservationService.getValidTemporaryReservationById(id);
  }

  @CustomerSecured
  @PutMapping("/checkout/init/{id}/update_owner")
  public ReservationDto saveReservationOwner(final @PathVariable @PositiveOrZero long id) {
    return reservationService.saveReservationOwnerInfo(id);
  }

  @LogAround(jsonInput = true)
  @PostMapping("/checkout/init/payment/authorize")
  public ResourceAuthorizeResponseDto authorizeReservationPayment(
      final @RequestParam(defaultValue = "paypal") String party,
      final @Valid @RequestBody ReservationCheckOutFinishRequestDto
              reservationCheckOutFinishRequestDto) {
    final ReservationDto tempReservation =
        reservationService.saveTemporaryReservation(reservationCheckOutFinishRequestDto);

    final ResourceAuthorizeResponseDto response = new ResourceAuthorizeResponseDto();

    final ServiceParty serviceParty =
        fromString(party)
            .orElseThrow(
                () ->
                    InvalidParamException.builder()
                        .query("party.invalid", "party", party, "Party is not correct")
                        .build());

    payingService = payingServiceFactory.getPayingService(serviceParty);

    final String approvalURL = payingService.getApproveURLFromPaymentCreation(tempReservation);
    response.setApprovalURL(approvalURL);

    return response;
  }

  @LogAround(jsonInput = true)
  @PostMapping("/checkout/finish/paypal")
  public ReservationDto checkOutFinishPaypal(
      final @Valid @RequestBody PaypalPaymentAuthorizeDto paypalPaymentAuthorizeDto) {
    final String orderId = paypalPaymentAuthorizeDto.getToken();
    final String payerId = paypalPaymentAuthorizeDto.getPayerId();

    payingService = payingServiceFactory.getPayingService(PAYPAL);

    try {
      PaymentResponse payment = payingService.executePayment(orderId, payerId);
      long temporaryReservationId = payment.getInvoiceId();

      return reservationService.saveNewReservationFromTemporary(
          temporaryReservationId, PaymentMethodCode.PAYPAL);
    } catch (PaypalRestException e) {
      throw new ServerErrorException(e.getMessage(), e);
    }
  }

  @PortalSecured
  @LogAround(output = false, jsonInput = true)
  @GetMapping
  public ResponseEntity<List<ReservationDto>> getReservationListByDateRange(
      final @RequestParam LocalDate checkInAt,
      final @RequestParam LocalDate checkOutAt,
      final @RequestParam(required = false, defaultValue = "") String statuses,
      final @RequestParam(required = false, defaultValue = "") String personalId,
      final Pageable pageable) {
    if (checkOutAt.isBefore(checkInAt))
      throw new RequestParamInvalidException("Date range is invalid", "dateRange", "invalid");

    final Page<ReservationDto> content =
        reservationService.getReservationsFromDateRange(
            checkInAt, checkOutAt, statuses, personalId, pageable);

    return PaginationUtil.createPaginationResponse(content);
  }

  @LogAround(output = false)
  @GetMapping("/detail/{id}")
  public ReservationDto getDetailOfReservation(final @PathVariable @PositiveOrZero long id) {
    return reservationService.getDetailOfReservation(id, reservationMapper::toDto);
  }

  @PortalSecured
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/detail/{id}/rental/save")
  public RentalDto saveNewRentalFromReservation(
      final @PathVariable @PositiveOrZero long id,
      final @Valid @RequestBody ReservationSaveToRentalRequestDto
              reservationSaveToRentalRequestDto) {
    if (!reservationSaveToRentalRequestDto.isCustomersAllocatedValid()) {
      throw new CustomerRentalDuplicatedException();
    }
    return reservationService.saveNewRentalFromReservation(id, reservationSaveToRentalRequestDto);
  }

  @PortalSecured
  @PutMapping("/{reservationId}/cancel")
  public ReservationDto cancelReservation(
      final @PathVariable @PositiveOrZero long reservationId,
      final @Valid @RequestBody ReservationCancelRequestDto cancelRequestDto) {
    return reservationService.cancelReservation(reservationId, cancelRequestDto);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/save")
  public ReservationDto saveReservation(
      final @Valid @RequestBody ReservationCreateDto reservationCreateDto) {
    return reservationService.saveReservation(reservationCreateDto);
  }

  @PutMapping("/{id}/update")
  public ReservationDto updateReservation(
      final @PathVariable @PositiveOrZero long id,
      final @Valid @RequestBody ReservationUpdateDto reservationUpdateDto) {
    verifyDateCheck(reservationUpdateDto.getCheckInAt(), reservationUpdateDto.getCheckOutAt());
    return reservationService.updateReservation(id, reservationUpdateDto);
  }

  private void verifyDateCheck(final LocalDate checkInAt, final LocalDate checkOutAt) {
    final LocalDate now = LocalDate.now();

    if (checkInAt.isBefore(now) || checkOutAt.isBefore(now) || !checkInAt.isBefore(checkOutAt)) {
      throw new RequestParamInvalidException("Date check is invalid", "dateCheck", "invalid");
    }
  }
}