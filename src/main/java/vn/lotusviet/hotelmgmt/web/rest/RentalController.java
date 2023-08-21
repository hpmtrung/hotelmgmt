package vn.lotusviet.hotelmgmt.web.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.annotation.security.PortalSecured;
import vn.lotusviet.hotelmgmt.exception.CustomerRentalDuplicatedException;
import vn.lotusviet.hotelmgmt.exception.RequestParamInvalidException;
import vn.lotusviet.hotelmgmt.exception.entity.RentalDetailUpdateIllegalException;
import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceDto;
import vn.lotusviet.hotelmgmt.model.dto.rental.*;
import vn.lotusviet.hotelmgmt.model.dto.reservation.RentabilityRoomsMappingDto;
import vn.lotusviet.hotelmgmt.model.dto.room.RoomFloorMappingDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceUsageCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceUsagePayDto;
import vn.lotusviet.hotelmgmt.service.RentalService;
import vn.lotusviet.hotelmgmt.util.PaginationUtil;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static vn.lotusviet.hotelmgmt.web.rest.RentalController.URL_PREFIX;

@RestController
@RequestMapping(URL_PREFIX)
public class RentalController extends AbstractController {

  public static final String URL_PREFIX = "/api/v1/rental";

  private final RentalService rentalService;

  public RentalController(RentalService rentalService) {
    this.rentalService = rentalService;
  }

  @PortalSecured
  @GetMapping
  public ResponseEntity<List<RentalBasicDto>> getRentalsByFilterOptions(
      final @RequestParam LocalDate startDate,
      final @RequestParam LocalDate endDate,
      final @RequestParam(required = false, defaultValue = "") String statuses,
      final @RequestParam(required = false, defaultValue = "") String personalId,
      final Pageable pageable) {
    final RentalFilterOption filterOption =
        new RentalFilterOption(personalId, statuses, startDate, endDate);
    final Page<RentalBasicDto> page = rentalService.getRentalByFilterOption(filterOption, pageable);
    return PaginationUtil.createPaginationResponse(page);
  }

  @PortalSecured
  @GetMapping("/verify_customer_rented")
  public ResponseEntity<Object> verifyCustomersRentedByPersonalIdList(
      final @RequestParam String personalIdsString) {
    final List<String> personalIds =
        Arrays.stream(personalIdsString.trim().toLowerCase().split("_"))
            .collect(Collectors.toList());
    return ResponseEntity.ok(Map.of("isRented", rentalService.isCustomersRented(personalIds)));
  }

  @PortalSecured
  @GetMapping("/active")
  public ResponseEntity<List<RentalBasicDto>> getActiveRentals(
      final @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable
              pageable) {
    final Page<RentalBasicDto> page = rentalService.getActiveRentals(pageable);
    return PaginationUtil.createPaginationResponse(page);
  }

  @PortalSecured
  @GetMapping("/{id}")
  public RentalDto getDetailOfRental(final @PathVariable long id) {
    return rentalService.getDetailOfRental(id);
  }

  @PortalSecured
  @ResponseStatus(HttpStatus.CREATED)
  @LogAround(jsonInput = true)
  @PostMapping("/save")
  public RentalDto saveNewRental(final @Valid @RequestBody RentalCreateDto rentalCreateDto) {
    if (rentalCreateDto.getAllCustomers().stream()
        .noneMatch(
            c -> c.getPersonalId().equalsIgnoreCase(rentalCreateDto.getMainCustomerPersonalId()))) {
      throw new RequestParamInvalidException(
          "Main customer personalId is not existed in customer list",
          "mainCustomerPersonalId",
          "notExisted");
    }
    checkDateCheckRentalValid(rentalCreateDto.getCheckInAt(), rentalCreateDto.getCheckOutAt());
    if (!rentalCreateDto.isCustomersAllocatedValid()) {
      throw new CustomerRentalDuplicatedException();
    }
    return rentalService.saveNewRental(rentalCreateDto);
  }

  @PortalSecured
  @LogAround
  @GetMapping("/{rentalId}/details/pay/review")
  public RentalPayingReviewResponseDto getPayingRentalDetailsReview(
      final @PathVariable long rentalId,
      final @Valid RentalDetailReviewRequestDto rentalDetailReviewRequestDto) {
    if (Arrays.stream(rentalDetailReviewRequestDto.getDetailIds().split("_"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .count()
        < 1) {
      throw new RequestParamInvalidException("RoomMapping Ids invalid", "details", "invalid");
    }

    final String reviewDetailIdsAsString = rentalDetailReviewRequestDto.getDetailIds();
    final List<Long> expectedPaidRentalDetailIds =
        Arrays.stream(reviewDetailIdsAsString.split("_"))
            .map(Long::parseLong)
            .collect(Collectors.toList());
    return rentalService.getRentalDetailPayingReview(
        rentalId, expectedPaidRentalDetailIds);
  }

  @PortalSecured
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/{rentalId}/details/pay")
  public InvoiceDto payingRentalDetails(
      final @Positive @PathVariable long rentalId,
      final @Valid @RequestBody RentalDetailPayingRequestDto rentalDetailPayingRequestDto) {
    return rentalService.payingRentalDetails(rentalId, rentalDetailPayingRequestDto);
  }

  @PortalSecured
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/{rentalId}/pay")
  public InvoiceDto payingRental(
      final @PathVariable long rentalId,
      final @Valid @RequestBody RentalPayingRequestDto rentalPayingRequestDto) {
    return rentalService.payingAllOfRental(rentalId, rentalPayingRequestDto);
  }

  @PortalSecured
  @GetMapping("/detail/{rentalDetailId}")
  public RentalDetailDto getRentalDetail(final @PathVariable long rentalDetailId) {
    return rentalService.getRentalDetailById(rentalDetailId);
  }

  @PortalSecured
  @GetMapping("/detail/{rentalDetailId}/room/change")
  public RoomFloorMappingDto getRentalDetailRoomChangeSuggestions(
      final @PathVariable long rentalDetailId, final @RequestParam boolean isSameSuite) {
    return new RoomFloorMappingDto(
        rentalService.getRentalDetailRoomChangeSuggestions(rentalDetailId, isSameSuite));
  }

  @PortalSecured
  @GetMapping("/detail/{rentalDetailId}/room/change/checkout_review")
  public RentalPayingReviewResponseDto getRentalDetailPayingReviewForRoomChangeDifferentSuite(
      final @PathVariable long rentalDetailId) {
    return rentalService.getRentalDetailPayingReviewForRoomChangeDifferentSuite(rentalDetailId);
  }

  @PortalSecured
  @PutMapping("/detail/{rentalDetailId}/room/change/save")
  public RentalDetailDto saveRentalDetailRoomChange(
      final @PathVariable long rentalDetailId,
      final @RequestParam int roomId,
      final @RequestParam boolean isSameSuite) {
    if (isSameSuite) {
      return rentalService.changeRoomSameSuiteForRentalDetail(rentalDetailId, roomId);
    } else {
      return rentalService.changeRoomDifferentSuiteForRentalDetail(rentalDetailId, roomId);
    }
  }

  @PortalSecured
  @PutMapping("/detail/{rentalDetailId}/service/pay")
  public RentalDetailDto payingServiceUsageDetails(
      final @PathVariable long rentalDetailId,
      final @Valid @RequestBody ServiceUsagePayDto serviceUsagePayDto) {
    return rentalService.payingServiceUsageDetail(rentalDetailId, serviceUsagePayDto);
  }

  @PortalSecured
  @GetMapping("/detail/{rentalDetailId}/update/max_checkout_date")
  public String getRentalDetailUpdateCheckOutMaxDate(final @PathVariable long rentalDetailId) {
    return rentalService
        .getRentalDetailMaximumCheckOutDate(rentalDetailId)
        .map(LocalDate::toString)
        .orElse(null);
  }

  @PortalSecured
  @PutMapping("/detail/{rentalDetailId}/update")
  public RentalDetailDto updateRentalDetail(
      final @PathVariable Long rentalDetailId,
      final @Valid @RequestBody RentalDetailUpdateDto rentalDetailUpdateDto) {
    if (rentalDetailUpdateDto.getCheckOutAt() != null) {
      final LocalDate maxCheckOutDate =
          rentalService.getRentalDetailMaximumCheckOutDate(rentalDetailId).orElseThrow();
      if (maxCheckOutDate.isBefore(rentalDetailUpdateDto.getCheckOutAt().toLocalDate())) {
        throw new RentalDetailUpdateIllegalException("Checkout date is invalid");
      }
    }
    return rentalService.updateRentalDetail(rentalDetailId, rentalDetailUpdateDto);
  }

  @PortalSecured
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/detail/{rentalDetailId}/service/add")
  public RentalDetailDto addServiceUsageDetails(
      final @PathVariable long rentalDetailId,
      final @Valid @RequestBody ServiceUsageCreateDto serviceUsageCreateDto) {
    return rentalService.addServiceUsageDetails(rentalDetailId, serviceUsageCreateDto);
  }

  @PortalSecured
  @GetMapping("/room_mapping/reservation/{id}")
  public RentabilityRoomsMappingDto getRentalableRoomMappingFromReservation(
      final @PathVariable long id) {
    return rentalService.getRentabilityRoomMappingFromReservation(id);
  }

  private void checkDateCheckRentalValid(
      final LocalDateTime checkInAt, final LocalDateTime checkOutAt) {
    final LocalDateTime now = LocalDateTime.now();

    if (checkInAt.isAfter(now) || checkOutAt.isBefore(now) || checkInAt.isAfter(checkOutAt)) {
      throw new RequestParamInvalidException("Date check are invalid", "dateCheck", "invalid");
    }
  }
}