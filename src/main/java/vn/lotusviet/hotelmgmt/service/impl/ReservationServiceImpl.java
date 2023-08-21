package vn.lotusviet.hotelmgmt.service.impl;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.exception.NoReservableSuiteFoundException;
import vn.lotusviet.hotelmgmt.exception.RequestParamInvalidException;
import vn.lotusviet.hotelmgmt.exception.ReservationNotFoundException;
import vn.lotusviet.hotelmgmt.exception.entity.*;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalDto;
import vn.lotusviet.hotelmgmt.model.dto.reservation.*;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationSaveToRentalRequestDto.RoomMapping;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationSaveToRentalRequestDto.SuiteMapping;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteReservationDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteSearchRequestDto;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;
import vn.lotusviet.hotelmgmt.model.entity.reservation.Reservation;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationDetail;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatus;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatusCode;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;
import vn.lotusviet.hotelmgmt.repository.checkin.RentalRepository;
import vn.lotusviet.hotelmgmt.repository.checkin.ReservationRepository;
import vn.lotusviet.hotelmgmt.repository.checkin.ReservationStatusRepository;
import vn.lotusviet.hotelmgmt.repository.room.SuiteRepository;
import vn.lotusviet.hotelmgmt.service.*;
import vn.lotusviet.hotelmgmt.service.factory.RentalFactory;
import vn.lotusviet.hotelmgmt.service.factory.ReservationDetailFactory;
import vn.lotusviet.hotelmgmt.service.factory.ReservationFactory;
import vn.lotusviet.hotelmgmt.service.factory.impl.DefaultReservationFactory;
import vn.lotusviet.hotelmgmt.service.mapper.RentalMapper;
import vn.lotusviet.hotelmgmt.service.mapper.ReservationMapper;
import vn.lotusviet.hotelmgmt.util.SecurityUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatusCode.*;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

  private static final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

  private static final PaymentMethodCode DEFAULT_PAYMENT_METHOD_CODE = PaymentMethodCode.PAYPAL;

  private static final List<ReservationStatusCode> CANCELABLE_STATUS_CODES =
      List.of(EXPIRED, NO_RENTED);

  private static final List<ReservationStatusCode> UPDATEABLE_RESERVATION_STATUS_CODES =
      List.of(TEMPORARY, COMPLETE_DEPOSIT, EXPIRED, NO_RENTED);

  private static final Comparator<SuiteReservationDto> SORT_SUITE_BY_MAX_OCCUPATION_ASC_COMPARATOR =
      Comparator.comparing(SuiteReservationDto::getMaxOccupation);

  private static final Comparator<RoomReservation>
      SORT_REQUIRED_OCCUPATION_ITEM_BY_INDEX_ASC_COMPARATOR =
          Comparator.comparingInt(RoomReservation::getIndex);

  private static final Comparator<RoomReservation>
      SORT_REQUIRED_OCCUPATION_ITEM_BY_VALUE_DESC_COMPARATOR =
          Comparator.comparing(RoomReservation::getOccupation, Comparator.reverseOrder());

  private static final Comparator<SuiteReservationDto>
      SORT_SUITE_RESERVATION_BY_PRICE_ASC_COMPARATOR =
          Comparator.comparingInt(SuiteReservationDto::getOriginalPrice);

  private static final Comparator<SuiteReservationDto>
      SORT_SUITE_RESERVATION_BY_PRICE_DESC_COMPARATOR =
          Comparator.comparing(SuiteReservationDto::getOriginalPrice, Comparator.reverseOrder());

  private final SuiteRepository suiteRepository;
  private final ReservationRepository reservationRepository;
  private final ReservationStatusRepository reservationStatusRepository;
  private final ReservationMapper reservationMapper;
  private final PromotionService promotionService;
  private final RoomService roomService;
  private final CustomerService customerService;
  private final RentalMapper rentalMapper;
  private final RentalRepository rentalRepository;
  private final CommonService commonService;
  private final ReservationFactory reservationFactory;
  private final ReservationDetailFactory reservationDetailFactory;
  private final RentalFactory rentalFactory;

  public ReservationServiceImpl(
      SuiteRepository suiteRepository,
      ReservationRepository reservationRepository,
      ReservationStatusRepository reservationStatusRepository,
      ReservationMapper reservationMapper,
      PromotionService promotionService,
      RoomService roomService,
      CustomerService customerService,
      RentalMapper rentalMapper,
      RentalRepository rentalRepository,
      CommonService commonService,
      DefaultReservationFactory reservationFactory,
      ReservationDetailFactory reservationDetailFactory,
      RentalFactory rentalFactory) {
    this.suiteRepository = suiteRepository;
    this.reservationRepository = reservationRepository;
    this.reservationStatusRepository = reservationStatusRepository;
    this.reservationMapper = reservationMapper;
    this.promotionService = promotionService;
    this.roomService = roomService;
    this.customerService = customerService;
    this.rentalMapper = rentalMapper;
    this.rentalRepository = rentalRepository;
    this.commonService = commonService;
    this.reservationFactory = reservationFactory;
    this.reservationDetailFactory = reservationDetailFactory;
    this.rentalFactory = rentalFactory;
  }

  private static int searchSuiteMatchOccupation(
      final int[] suiteOccupations, final RoomReservation item) {
    int l = 0;
    int h = suiteOccupations.length - 1;
    int mid;
    while (l < h) {
      mid = l + (h - l) / 2;
      if (item.getOccupation() <= suiteOccupations[mid]) h = mid;
      else l = mid + 1;
    }
    return l;
  }

  @Override
  @LogAround(jsonInput = true)
  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<SuiteReservationDto> getAvailableSuitesByDate(
      final LocalDate checkInAt,
      final LocalDate checkOutAt,
      final SuiteSearchRequestDto filters,
      final Sort sort) {
    final Comparator<SuiteReservationDto> comparator =
        getSortSuiteReservationByPriceComparator(sort);
    final List<SuiteReservationDto> suites =
        suiteRepository.findAvailableSuitesForReservation(checkInAt, checkOutAt, filters).stream()
            .sorted(comparator)
            .collect(Collectors.toList());
    suites.forEach(
        suite ->
            suite.setPromotions(
                promotionService.getSuitePromotionsFromDate(suite.getId(), checkInAt, checkOutAt)));
    return suites;
  }

  @LogAround
  @Transactional(readOnly = true)
  public ReservationSearchResponseDto getReservableSuites(
      final LocalDate checkInAt, final LocalDate checkOutAt, final List<Integer> occupations) {
    Objects.requireNonNull(checkInAt);
    Objects.requireNonNull(checkOutAt);
    Objects.requireNonNull(occupations);

    final List<SuiteReservationDto> availableSuites =
        suiteRepository.findAvailableSuitesForReservation(checkInAt, checkOutAt);

    availableSuites.sort(SORT_SUITE_BY_MAX_OCCUPATION_ASC_COMPARATOR);

    final List<RoomReservation> requiredOccupations = new ArrayList<>();

    // Use record to keep index
    for (int occupationIndex = 0; occupationIndex < occupations.size(); occupationIndex++) {
      int occupation = occupations.get(occupationIndex);
      requiredOccupations.add(new RoomReservation(occupationIndex, occupation));
    }

    // Sort occupations desc
    requiredOccupations.sort(SORT_REQUIRED_OCCUPATION_ITEM_BY_VALUE_DESC_COMPARATOR);

    final int totalSuite = availableSuites.size();
    final int[] suiteOccupations = new int[totalSuite];
    final int[] availableRoomNums = new int[totalSuite];

    for (int suiteIndex = 0; suiteIndex < totalSuite; suiteIndex++) {
      final SuiteReservationDto suite = availableSuites.get(suiteIndex);
      suiteOccupations[suiteIndex] = suite.getMaxOccupation();
      availableRoomNums[suiteIndex] = suite.getEmptyRoomNum();
    }

    for (final RoomReservation item : requiredOccupations) {
      int matchIndex = searchSuiteMatchOccupation(suiteOccupations, item);
      if (item.getOccupation() > suiteOccupations[matchIndex]) {
        throw new NoReservableSuiteFoundException("Could not find reservable suites");
      }
      for (int suiteIndex = matchIndex; suiteIndex < totalSuite; suiteIndex++) {
        if (availableRoomNums[suiteIndex] > 0) {
          item.getSelectedSuites().add(availableSuites.get(suiteIndex));
          availableRoomNums[suiteIndex]--;
        }
      }
    }

    // Reorder `requiredOccupations` array to the original state
    requiredOccupations.sort(SORT_REQUIRED_OCCUPATION_ITEM_BY_INDEX_ASC_COMPARATOR);

    // Fill result with the selected suites
    final ReservationSearchResponseDto result = new ReservationSearchResponseDto();

    // Sort the list of selected suite of each required room by price
    for (final RoomReservation item : requiredOccupations) {
      final List<SuiteReservationDto> selectedSuites =
          item.getSelectedSuites().stream()
              .sorted(SORT_SUITE_RESERVATION_BY_PRICE_ASC_COMPARATOR)
              .collect(Collectors.toList());
      for (final SuiteReservationDto suite : selectedSuites) {
        suite.setPromotions(
            promotionService.getSuitePromotionsFromDate(suite.getId(), checkInAt, checkOutAt));
      }
      result.getReservableSuites().addAll(selectedSuites);
      result
          .getOccupationMapSuiteIds()
          .add(
              selectedSuites.stream().map(SuiteReservationDto::getId).collect(Collectors.toList()));
    }

    return result;
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public Page<ReservationDto> getReservationsOfCustomer(
      final String personalId,
      final Pageable pageable,
      final Function<Reservation, ReservationDto> mappingFunction) {
    Objects.requireNonNull(personalId);
    Objects.requireNonNull(pageable);
    Objects.requireNonNull(mappingFunction);
    return reservationRepository.findByOwner_PersonalId(personalId, pageable).map(mappingFunction);
  }

  @LogAround
  @Transactional(readOnly = true)
  public boolean isReservationExistByIdAndOwnerId(
      final long reservationId, final @NotNull String reservationOwnerId) {
    Objects.requireNonNull(reservationOwnerId);
    return reservationRepository.existsByIdAndOwner_PersonalId(reservationId, reservationOwnerId);
  }

  @Override
  @LogAround(jsonInput = true, jsonOutput = true)
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public ReservationDto saveNewTemporaryReservation(
      final ReservationCheckOutInitRequestDto reservationCheckOutInitRequest) {
    Objects.requireNonNull(reservationCheckOutInitRequest);

    Customer owner = null;
    if (SecurityUtil.isAuthenticated()) {
      owner = customerService.getLoginCustomer();
    }

    final Reservation temporaryReservation =
        reservationFactory.createTemporaryReservation(
            reservationCheckOutInitRequest.getCheckInAt(),
            reservationCheckOutInitRequest.getCheckOutAt(),
            DEFAULT_PAYMENT_METHOD_CODE,
            owner);

    final Map<Integer, Integer> suiteMapToRoomNum =
        reservationCheckOutInitRequest.getSuiteIdMapping();

    saveNewReservationDetailsFromSuiteIdMapping(temporaryReservation, suiteMapToRoomNum);

    log.debug("Save new temporary reservation: {}", temporaryReservation);

    return reservationMapper.toDto(reservationRepository.save(temporaryReservation));
  }

  @Override
  @LogAround(jsonOutput = true)
  @Transactional(readOnly = true)
  public ReservationDto getValidTemporaryReservationById(final long id) {
    final Reservation reservation = getReservationByIdWithPromotionInfo(id);
    verifyTemporaryReservationIsValid(reservation);
    return reservationMapper.toDto(reservation);
  }

  @Override
  @LogAround
  public ReservationDto saveReservationOwnerInfo(final long id) {
    Reservation reservation = getReservationById(id);

    verifyTemporaryReservationIsValid(reservation);

    if (reservation.getOwner() == null) {
      reservation.setOwner(customerService.getLoginCustomer());
      reservation = reservationRepository.save(reservation);
      log.debug("Save the owner (login) for reservation [{}]", reservation.getOwner());
    }

    log.debug("The reservation already has owner, no update!");
    return reservationMapper.toDto(reservation);
  }

  @Override
  @LogAround(jsonOutput = true)
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public ReservationDto saveNewReservationFromTemporary(
      final long temporaryReservationId, final PaymentMethodCode paymentMethodCode) {
    final Reservation temporaryReservation =
        getReservationByIdWithPromotionInfo(temporaryReservationId);

    verifyTemporaryReservationIsValid(temporaryReservation);

    // Save status to COMPLETE_DEPOSIT
    // Save payment method
    temporaryReservation
        .setStatus(commonService.getReservationStatusByCode(ReservationStatusCode.COMPLETE_DEPOSIT))
        .setPaymentMethod(commonService.getPaymentMethodByCode(paymentMethodCode));

    return reservationMapper.toDto(reservationRepository.save(temporaryReservation));
  }

  @Override
  @LogAround(jsonOutput = true, jsonInput = true)
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public ReservationDto saveTemporaryReservation(
      final ReservationCheckOutFinishRequestDto checkOutFinishData) {
    Objects.requireNonNull(checkOutFinishData);

    final Reservation temporaryReservation =
        getReservationByIdWithPromotionInfo(checkOutFinishData.getReservationId());

    verifyTemporaryReservationIsValid(temporaryReservation);

    // Update owner
    final Customer customer =
        customerService.saveOrUpdateCustomer(checkOutFinishData.getCustomer());
    temporaryReservation.setOwner(customer);

    // Update special requirement
    updateReservationSpecialRequirements(
        temporaryReservation, checkOutFinishData.getSpecialRequirements());

    verifyReservationDepositAmountIsValid(
        temporaryReservation.getDepositAmount(), checkOutFinishData.getDepositAmount());

    temporaryReservation.setDepositAmount(checkOutFinishData.getDepositAmount());

    // Update payment method
    temporaryReservation.setPaymentMethod(
        commonService.getPaymentMethodByCode(checkOutFinishData.getPaymentMethodCode()));

    return reservationMapper.toDto(reservationRepository.save(temporaryReservation));
  }

  @Override
  @LogAround(output = false)
  @Transactional(readOnly = true)
  public Page<ReservationDto> getReservationsFromDateRange(
      final LocalDate checkInAt,
      final LocalDate checkOutAt,
      final String statuses,
      final String personalId,
      final Pageable pageable) {
    return reservationRepository
        .findByDateRange(checkInAt, checkOutAt, statuses, personalId, pageable)
        .map(this::setReservationDetailPromotions);
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public ReservationDto getDetailOfReservation(
      final long reservaionId,
      final @NotNull Function<Reservation, ReservationDto> mapperFunction) {
    Objects.requireNonNull(mapperFunction);
    return mapperFunction.apply(getReservationByIdWithPromotionInfo(reservaionId));
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public ReservationDto getReservationWithoutPromotionInfo(final long id) {
    return reservationRepository
        .findById(id)
        .map(reservationMapper::toDto)
        .orElseThrow(() -> new ReservationNotFoundException(id));
  }

  @Override
  @LogAround
  public RentalDto saveNewRentalFromReservation(
      final long id, final ReservationSaveToRentalRequestDto saveToRentalRequest) {
    Objects.requireNonNull(saveToRentalRequest);

    final Reservation reservation = getReservationById(id);
    setReservationDetailPromotions(reservation);

    verifyReservationCanSaveToRental(reservation, saveToRentalRequest);

    reservation.setStatus(getReservationStatusByCode(ReservationStatusCode.RENTED));

    Rental newRental =
        rentalFactory.createRentalFromReservation(
            reservation, saveToRentalRequest.getRentalDiscountAmount());

    for (final SuiteMapping suiteMapping : saveToRentalRequest.getSuiteMappings()) {
      final int suiteId = suiteMapping.getSuiteId();

      final ReservationDetail reservationDetailOfSuite =
          getReservationDetailOfSuite(reservation, suiteId);

      for (final RoomMapping roomMapping : suiteMapping.getRoomMappings()) {
        final int roomId = roomMapping.getRoomId();

        final Set<Customer> customers =
            roomMapping.getCustomers().stream()
                .map(customerService::saveOccupiedCustomerIfNotExist)
                .collect(Collectors.toSet());

        final RentalDetail newDetail =
            rentalFactory.createRentalDetail(
                newRental.getCheckInAt(),
                newRental.getCheckOutAt(),
                roomId,
                reservationDetailOfSuite.getSuitePrice(),
                reservationDetailOfSuite.getVat(),
                0,
                0,
                customers);

        newRental.addDetail(newDetail);
      }
    }

    newRental = rentalRepository.save(newRental);

    for (final RentalDetail detail : newRental.getDetails()) {
      detail.setPromotions(
          promotionService.getSuitePromotionsFromDate(
              detail.getRoom().getSuiteId(),
              detail.getCheckInAt().toLocalDate(),
              detail.getCheckOutAt().toLocalDate()));
    }

    reservation.setRental(newRental);
    return rentalMapper.toDto(newRental);
  }

  @Override
  @LogAround(jsonInput = true, jsonOutput = true)
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public ReservationDto saveReservation(final ReservationCreateDto reservationCreate) {
    Objects.requireNonNull(reservationCreate);

    final Customer owner = customerService.saveOrUpdateCustomer(reservationCreate.getCustomer());
    Reservation newReservation = reservationFactory.createReservation(reservationCreate, owner);
    // Save details
    final Map<Integer, Integer> suiteIdMapping = reservationCreate.getSuiteIdMapping();
    saveNewReservationDetailsFromSuiteIdMapping(newReservation, suiteIdMapping);
    newReservation = reservationRepository.save(newReservation);
    log.debug("Saving a new direct reservation: {}", newReservation);
    return reservationMapper.toDto(newReservation);
  }

  @Override
  @LogAround(jsonInput = true, jsonOutput = true)
  public ReservationDto updateReservation(
      final long reservationId, final ReservationUpdateDto updateData) {
    Objects.requireNonNull(updateData);

    final Reservation existReservation = getReservationById(reservationId);

    verifyReservationUpdatability(existReservation);

    final LocalDate updateCheckInAt = updateData.getCheckInAt();
    final LocalDate updateCheckOutAt = updateData.getCheckOutAt();

    updateReservationDateCheck(existReservation, updateCheckInAt, updateCheckOutAt);

    // Update suite id mapping
    if (updateData.getSuiteIdMapping() != null) {
      updateReservationDetailsFromSuiteIdMapping(existReservation, updateData.getSuiteIdMapping());
    }

    // Update special requirements
    if (updateData.getSpecialRequirements() != null) {
      updateReservationSpecialRequirements(existReservation, updateData.getSpecialRequirements());
    }

    // Update time elapsed minutes
    if (updateData.getTimeElapsedMins() != null) {
      updateReservationTimeElapsedMinutes(updateData, existReservation);
    }

    // Update owner
    if (updateData.getCustomer() != null) {
      existReservation.setOwner(customerService.saveOrUpdateCustomer(updateData.getCustomer()));
    }

    setReservationDetailPromotions(existReservation);
    existReservation.updateTotal();

    return reservationMapper.toDto(reservationRepository.save(existReservation));
  }

  @Override
  @LogAround
  public ReservationDto cancelReservation(
      final long reservationId, final ReservationCancelRequestDto cancelRequestDto) {
    Objects.requireNonNull(cancelRequestDto);

    final Reservation existReservation = getReservationByIdWithPromotionInfo(reservationId);
    verifyReservationCanCancel(existReservation);

    existReservation.setStatus(getReservationStatusByCode(CANCEL));
    if (cancelRequestDto.getRefund() != null) {
      existReservation.setRefund(cancelRequestDto.getRefund());
    }
    return reservationMapper.toDto(reservationRepository.save(existReservation));
  }

  @Transactional(readOnly = true)
  public Reservation getReservationById(final long reservationId) {
    return reservationRepository
        .findById(reservationId)
        .orElseThrow(() -> new ReservationNotFoundException(reservationId));
  }

  @Transactional(readOnly = true)
  public Reservation getReservationByIdWithPromotionInfo(final long reservationId) {
    return reservationRepository
        .findById(reservationId)
        .map(this::setReservationDetailPromotions)
        .orElseThrow(() -> new ReservationNotFoundException(reservationId));
  }

  private Comparator<SuiteReservationDto> getSortSuiteReservationByPriceComparator(
      final Sort sort) {
    final Sort.Order sortOrder = sort.getOrderFor("price");
    final boolean isSortByPriceDesc = sortOrder != null && sortOrder.getDirection().isDescending();
    if (isSortByPriceDesc) {
      return SORT_SUITE_RESERVATION_BY_PRICE_DESC_COMPARATOR;
    } else {
      return SORT_SUITE_RESERVATION_BY_PRICE_ASC_COMPARATOR;
    }
  }

  private void updateReservationTimeElapsedMinutes(
      final ReservationUpdateDto updateData, final Reservation existReservation) {
    final int updateTimeElapsedMinutes = updateData.getTimeElapsedMins();
    final ReservationStatusCode currentStatusCode = existReservation.getStatus().getCode();

    final boolean isReservationNotTemporary = !currentStatusCode.equals(TEMPORARY);
    if (isReservationNotTemporary) {
      throw new ReservationUpdateIllegalException(
          "Cannot update time elapsed minutes on the non-temporary reservation");
    }

    final boolean isNewTimeElapsedMinutesValueLessThanOldValue =
        existReservation.getTimeElapsedMins() > updateTimeElapsedMinutes;
    if (isNewTimeElapsedMinutesValueLessThanOldValue) {
      throw new ReservationUpdateIllegalException(
          "Cannot update time elapsed minutes, new value is smaller than old value");
    }

    final Instant newDeadline =
        existReservation.getCreatedAt().plus(updateTimeElapsedMinutes, ChronoUnit.MINUTES);

    final boolean isTimeElapsedMinutesInvalidWithCheckInDate =
        newDeadline
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .isAfter(existReservation.getCheckInAt());
    if (isTimeElapsedMinutesInvalidWithCheckInDate) {
      throw new ReservationUpdateIllegalException(
          "Cannot update time elapsed minutes, invalid with check in date");
    }

    log.debug(
        "Update reservation's time elapses minutes [old value: '{}', new value: '{}']",
        existReservation.getTimeElapsedMins(),
        updateTimeElapsedMinutes);
    existReservation.setTimeElapsedMins(updateTimeElapsedMinutes);

    if (newDeadline.isAfter(Instant.now())) {
      existReservation.setStatus(getReservationStatusByCode(TEMPORARY));
    }
  }

  private void verifyReservationCanCancel(final Reservation existReservation) {
    final ReservationStatusCode currentStatusCode = existReservation.getStatus().getCode();
    final boolean canCancel = !CANCELABLE_STATUS_CODES.contains(currentStatusCode);
    if (canCancel) {
      throw new ReservationCancelNotAllowException(
          String.format(
              "Cannot cancel reservation due to invalid status [current status '%s']",
              currentStatusCode));
    }
  }

  private void updateReservationSpecialRequirements(
      final Reservation existReservation, final String updateSpecialRequirements) {
    existReservation.setSpecialRequirements(updateSpecialRequirements.trim());
  }

  private void updateReservationDateCheck(
      final Reservation existReservation,
      final LocalDate updateCheckInAt,
      final LocalDate updateCheckOutAt) {
    final boolean isDateChanged =
        !existReservation.getCheckInAt().equals(updateCheckInAt)
            || !existReservation.getCheckOutAt().equals(updateCheckOutAt);

    if (isDateChanged) {
      existReservation.setCheckInAt(updateCheckInAt).setCheckOutAt(updateCheckOutAt);

      final boolean isReservationAtNoRentedStatus =
          existReservation.getStatus().getCode().equals(NO_RENTED);

      final boolean isUpdateCheckInAtInFuture = !updateCheckInAt.isBefore(LocalDate.now());

      if (isReservationAtNoRentedStatus && isUpdateCheckInAtInFuture) {
        // Save new status as complete deposit
        existReservation.setStatus(getReservationStatusByCode(COMPLETE_DEPOSIT));
        log.debug("Restore reservation status from 'NO_RENTED' to 'COMPLETE_DEPOSIT'.");
      }
    }
  }

  private void verifyReservationUpdatability(Reservation existReservation) {
    final ReservationStatusCode statusCode = existReservation.getStatus().getCode();
    if (!UPDATEABLE_RESERVATION_STATUS_CODES.contains(statusCode)) {
      throw new ReservationUpdateIllegalException(
          String.format(
              "Cannot update reservation due to invalid status (value: '%s')", statusCode));
    }
  }

  private ReservationDetail getReservationDetailOfSuite(
      final Reservation reservation, final int suiteId) {
    return reservation.getDetails().stream()
        .filter(d -> d.getSuite().getId().equals(suiteId))
        .findAny()
        .orElseThrow();
  }

  private void verifyReservationDepositAmountIsValid(
      final int minimumAmount, final int actualAmount) {
    if (minimumAmount > actualAmount) {
      throw new RequestParamInvalidException(
          "Reservation deposit is invalid", "depositAmount", "invalid");
    }
  }

  private void updateReservationDetailsFromSuiteIdMapping(
      final Reservation reservation, final Map<Integer, Integer> suiteIdMapping) {
    final List<SuiteReservationDto> reservableSuites =
        suiteRepository.findAvailableSuitesForReservation(
            reservation.getCheckInAt(),
            reservation.getCheckOutAt(),
            new SuiteSearchRequestDto().setReservationIdExcluded(reservation.getId()));

    verifySuiteIdMappingIsValid(reservableSuites, suiteIdMapping);

    final List<ReservationDetail> details = reservation.getDetails();

    if (details.isEmpty()) {
      throw new IllegalArgumentException("Reservation details should not be empty");
    }

    // Remove details not selected
    final List<ReservationDetail> removedDetails =
        details.stream()
            .filter(d -> !suiteIdMapping.containsKey(d.getSuite().getId()))
            .collect(Collectors.toList());

    removedDetails.forEach(reservation::removeDetail);

    // Update number of room per selected suite from mapping
    for (final Map.Entry<Integer, Integer> suiteIdAndNumRoomEntry : suiteIdMapping.entrySet()) {
      final int suiteId = suiteIdAndNumRoomEntry.getKey();
      final int numRoom = suiteIdAndNumRoomEntry.getValue();

      ReservationDetail detail =
          details.stream().filter(d -> d.getSuite().getId().equals(suiteId)).findAny().orElse(null);

      if (detail == null) {
        detail = createNewReservationDetail(reservation, suiteId, numRoom);
        reservation.addDetail(detail);
      } else {
        detail.setRoomNum(numRoom);
      }
    }
  }

  private void saveNewReservationDetailsFromSuiteIdMapping(
      final Reservation reservation, final Map<Integer, Integer> suiteIdMapping) {

    final List<SuiteReservationDto> reservableSuites =
        suiteRepository.findAvailableSuitesForReservation(
            reservation.getCheckInAt(), reservation.getCheckOutAt());
    verifySuiteIdMappingIsValid(reservableSuites, suiteIdMapping);

    final boolean isDetailsNotEmpty = !reservation.getDetails().isEmpty();

    if (isDetailsNotEmpty) {
      throw new IllegalArgumentException("Reservation details of new reservation should be empty.");
    }

    for (final Map.Entry<Integer, Integer> suiteIdAndNumRoomEntry : suiteIdMapping.entrySet()) {
      final int suiteId = suiteIdAndNumRoomEntry.getKey();
      final int numRoom = suiteIdAndNumRoomEntry.getValue();

      final ReservationDetail detail = createNewReservationDetail(reservation, suiteId, numRoom);
      reservation.addDetail(detail);
    }

    reservation.updateTotal();
  }

  private ReservationDetail createNewReservationDetail(
      final Reservation reservation, final int suiteId, final int numRoom) {
    final List<PromotionDto> detailPromotions =
        promotionService.getSuitePromotionsFromDate(
            suiteId, reservation.getCheckInAt(), reservation.getCheckOutAt());
    return reservationDetailFactory.create(suiteId, numRoom, detailPromotions);
  }

  private void verifySuiteIdMappingIsValid(
      final List<SuiteReservationDto> reservableSuites,
      final Map<Integer, Integer> suiteIdMapping) {

    for (final Map.Entry<Integer, Integer> suiteIdAndRoomNumEntry : suiteIdMapping.entrySet()) {
      final int suiteId = suiteIdAndRoomNumEntry.getKey();
      final int numRoom = suiteIdAndRoomNumEntry.getValue();

      // Find suite from `suiteId` or else throw exception
      final SuiteReservationDto suite =
          reservableSuites.stream()
              .filter(s -> s.getId() == suiteId)
              .findAny()
              .orElseThrow(
                  () ->
                      new ReservationUpdateIllegalException(
                          String.format(
                              "Suite id mapping at id %d is not found on reservable suite list",
                              suiteId)));

      final boolean isNumRoomAccepted = suite.getEmptyRoomNum() < numRoom;
      if (isNumRoomAccepted) {
        throw new ReservationUpdateIllegalException(
            String.format(
                "Suite id mapping at id %d has not enough empty room num (updated value: %d, current: %d)",
                suiteId, numRoom, suite.getEmptyRoomNum()));
      }
    }
  }

  private Reservation setReservationDetailPromotions(final Reservation reservation) {
    if (reservation == null) throw new NullPointerException();
    reservation
        .getDetails()
        .forEach(
            detail ->
                detail.setPromotions(
                    promotionService.getSuitePromotionsFromDate(
                        detail.getSuite().getId(),
                        reservation.getCheckInAt(),
                        reservation.getCheckOutAt())));
    return reservation;
  }

  private ReservationDto setReservationDetailPromotions(final ReservationDto reservation) {
    if (reservation.getDetails() == null) return reservation;
    reservation
        .getDetails()
        .forEach(
            detail ->
                detail.setPromotions(
                    promotionService.getSuitePromotionsFromDate(
                        detail.getSuite().getId(),
                        reservation.getCheckInAt(),
                        reservation.getCheckOutAt())));
    return reservation;
  }

  private ReservationStatus getReservationStatusByCode(final ReservationStatusCode code) {
    return reservationStatusRepository
        .findByCode(code)
        .orElseThrow(() -> new ReservationStatusNotFoundException(code));
  }

  private void verifyReservationCanSaveToRental(
      final Reservation reservation, final ReservationSaveToRentalRequestDto saveToRentalRequest) {
    final boolean isCheckInAtInValid = reservation.getCheckInAt().isAfter(LocalDate.now());
    if (isCheckInAtInValid) {
      throw new RentalSaveIllegalException("Check in date is not allowed");
    }

    final int currentDetailsSize = reservation.getDetails().size();
    final int requestNumSuite = saveToRentalRequest.getSuiteMappings().size();

    final boolean isDetailsSizeNotMatch = currentDetailsSize != requestNumSuite;
    if (isDetailsSizeNotMatch) {
      log.debug(
          "Number suite of reservation not match [current: {}, request value: {}]",
          currentDetailsSize,
          requestNumSuite);
      throw new RequestParamInvalidException("suiteMapping is invalid", "suiteMappings", "invalid");
    }

    final List<Integer> suiteIds = getSuiteIdsFromReservationDetails(reservation);

    for (final SuiteMapping suiteMapping : saveToRentalRequest.getSuiteMappings()) {
      final int suiteId = suiteMapping.getSuiteId();

      boolean isSuiteIdNotFound = !suiteIds.contains(suiteId);

      if (isSuiteIdNotFound) {
        throw new RequestParamInvalidException("SuiteId is not found", "suiteId", "notFound");
      }

      for (final RoomMapping roomMapping : suiteMapping.getRoomMappings()) {
        boolean isRoomIdInValid =
            !roomService.isRoomBelongToSuite(roomMapping.getRoomId(), suiteId);

        if (isRoomIdInValid) {
          throw new RequestParamInvalidException("RoomId is not matched", "roomId", "notMatched");
        }

        final Suite suite = roomService.getSuiteById(suiteId);

        final int suiteMaxOccupation = suite.getSuiteStyle().getMaxOccupation();
        final int numCustomerOfRoom = roomMapping.getCustomers().size();

        if (numCustomerOfRoom > suiteMaxOccupation) {
          throw new RequestParamInvalidException(
              "Room occupation is not accepted", "customers", "occupationNotAccepted");
        }
      }
    }
  }

  @NotNull
  private List<Integer> getSuiteIdsFromReservationDetails(final Reservation reservation) {
    return reservation.getDetails().stream()
        .map(ReservationDetail::getSuite)
        .map(Suite::getId)
        .collect(Collectors.toList());
  }

  private void verifyTemporaryReservationIsValid(final Reservation temporaryReservation) {
    final boolean isTimeout =
        temporaryReservation.getCreatedAt().until(Instant.now(), ChronoUnit.MINUTES)
            > temporaryReservation.getTimeElapsedMins();

    if (isTimeout) {
      throw new ReservationNotFoundException(temporaryReservation.getId());
    }

    final ReservationStatusCode status = temporaryReservation.getStatus().getCode();
    final boolean isNotAtTemporaryStatus = !status.equals(ReservationStatusCode.TEMPORARY);
    if (isNotAtTemporaryStatus) {
      throw new ReservationSaveIllegalStateException(
          String.format(
              "Temporary reservation has an incorrect status [value: '%s']", status.name()));
    }
  }

  private static class RoomReservation {
    private final int index;
    private final int occupation;
    private final Set<SuiteReservationDto> selectedSuites = new HashSet<>();

    public RoomReservation(int index, int occupation) {
      this.index = index;
      this.occupation = occupation;
    }

    public int getIndex() {
      return index;
    }

    public int getOccupation() {
      return occupation;
    }

    public Set<SuiteReservationDto> getSelectedSuites() {
      return selectedSuites;
    }

    @Override
    public String toString() {
      return "RoomOccupancyRecord{"
          + "index="
          + index
          + ", occupation="
          + occupation
          + ", suiteIds="
          + selectedSuites
          + '}';
    }
  }
}