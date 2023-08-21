package vn.lotusviet.hotelmgmt.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.exception.BadRequestException;
import vn.lotusviet.hotelmgmt.exception.RentalRoomMappingNotResolvedException;
import vn.lotusviet.hotelmgmt.exception.RequestParamInvalidException;
import vn.lotusviet.hotelmgmt.exception.entity.*;
import vn.lotusviet.hotelmgmt.model.adt.RentalDetailUpdatable;
import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceDto;
import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceType;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;
import vn.lotusviet.hotelmgmt.model.dto.rental.*;
import vn.lotusviet.hotelmgmt.model.dto.reservation.RentabilityRoomsMappingDto;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationSaveToRentalRequestDto.SuiteMapping;
import vn.lotusviet.hotelmgmt.model.dto.room.RoomDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceUsageCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceUsagePayDto;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.model.entity.rental.Invoice;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalPayment;
import vn.lotusviet.hotelmgmt.model.entity.reservation.Reservation;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationDetail;
import vn.lotusviet.hotelmgmt.model.entity.room.Room;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatusCode;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceUsageDetail;
import vn.lotusviet.hotelmgmt.repository.checkin.RentalDetailRepository;
import vn.lotusviet.hotelmgmt.repository.checkin.RentalRepository;
import vn.lotusviet.hotelmgmt.repository.checkout.InvoiceRepository;
import vn.lotusviet.hotelmgmt.repository.room.RoomRepository;
import vn.lotusviet.hotelmgmt.service.*;
import vn.lotusviet.hotelmgmt.service.factory.InvoiceFactory;
import vn.lotusviet.hotelmgmt.service.factory.RentalFactory;
import vn.lotusviet.hotelmgmt.service.factory.ServiceFactory;
import vn.lotusviet.hotelmgmt.service.mapper.EmployeeMapper;
import vn.lotusviet.hotelmgmt.service.mapper.InvoiceMapper;
import vn.lotusviet.hotelmgmt.service.mapper.RentalMapper;
import vn.lotusviet.hotelmgmt.service.mapper.RoomMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static vn.lotusviet.hotelmgmt.model.dto.rental.RentalPayingReviewResponseDto.UseMode.*;
import static vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationSaveToRentalRequestDto.RoomMapping;
import static vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatusCode.FULL_CHECKOUT;
import static vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatusCode.HALF_CHECKOUT;

@Service
@Transactional
public class RentalServiceImpl implements RentalService {

  private static final int RENTAL_DETAIL_MAXIMUM_CHECKOUT_DAY_THRESHOLD = 30;

  private static final Logger log = LoggerFactory.getLogger(RentalServiceImpl.class);

  private final RentalDetailRepository rentalDetailRepository;
  private final EmployeeService employeeService;
  private final RentalMapper rentalMapper;
  private final RentalRepository rentalRepository;
  private final CommonService commonService;
  private final PromotionService promotionService;
  private final EmployeeMapper employeeMapper;
  private final RoomRepository roomRepository;
  private final RoomMapper roomMapper;
  private final InvoiceRepository invoiceRepository;
  private final RoomService roomService;
  private final CustomerService customerService;
  private final ServiceFactory serviceFactory;
  private final InvoiceFactory invoiceFactory;
  private final RentalFactory rentalFactory;
  private final ReservationService reservationService;
  private final InvoiceMapper invoiceMapper;

  public RentalServiceImpl(
      RentalDetailRepository rentalDetailRepository,
      EmployeeService employeeService,
      RentalMapper rentalMapper,
      RentalRepository rentalRepository,
      CommonService commonService,
      PromotionService promotionService,
      EmployeeMapper employeeMapper,
      RoomRepository roomRepository,
      RoomMapper roomMapper,
      RoomService roomService,
      CustomerService customerService,
      InvoiceRepository invoiceRepository,
      ServiceFactory serviceFactory,
      InvoiceFactory invoiceFactory,
      RentalFactory rentalFactory,
      ReservationService reservationService,
      InvoiceMapper invoiceMapper) {
    this.rentalDetailRepository = rentalDetailRepository;
    this.employeeService = employeeService;
    this.rentalMapper = rentalMapper;
    this.rentalRepository = rentalRepository;
    this.commonService = commonService;
    this.promotionService = promotionService;
    this.employeeMapper = employeeMapper;
    this.roomRepository = roomRepository;
    this.roomMapper = roomMapper;
    this.roomService = roomService;
    this.customerService = customerService;
    this.invoiceRepository = invoiceRepository;
    this.serviceFactory = serviceFactory;
    this.invoiceFactory = invoiceFactory;
    this.rentalFactory = rentalFactory;
    this.reservationService = reservationService;
    this.invoiceMapper = invoiceMapper;
  }

  @Override
  @LogAround(output = false)
  @Transactional(readOnly = true)
  public Rental getRentalById(final long rentalId) {
    return rentalRepository
        .findById(rentalId)
        .map(
            rental -> {
              rental.getDetails().forEach(this::setRentalDetailPromotions);
              return rental;
            })
        .orElseThrow(() -> new RentalNotFoundException(rentalId));
  }

  @Override
  @LogAround(jsonOutput = true)
  @Transactional(readOnly = true)
  public Page<RentalBasicDto> getActiveRentals(final Pageable pageable) {
    return rentalRepository
        .findNotFullCheckoutCompleteRentals(pageable)
        .map(rentalMapper::toBasicDto);
  }

  @Override
  @LogAround(output = false, jsonInput = true)
  @Transactional(readOnly = true)
  public Page<RentalBasicDto> getRentalByFilterOption(
      final RentalFilterOption rentalFilterOption, final Pageable pageable) {
    Objects.requireNonNull(rentalFilterOption);
    Objects.requireNonNull(pageable);

    return rentalRepository.findByFilterOption(rentalFilterOption, pageable);
  }

  @Override
  @LogAround(jsonOutput = true)
  public RentalDto getDetailOfRental(final long rentalId) {
    return rentalMapper.toDto(getRentalById(rentalId));
  }

  @Override
  @LogAround(jsonOutput = true, jsonInput = true)
  public RentalDetailDto payingServiceUsageDetail(
      final long rentalDetailId, final ServiceUsagePayDto serviceUsagePayDto) {
    Objects.requireNonNull(serviceUsagePayDto);

    final RentalDetail rentalDetail = getRentalDetailEntityById(rentalDetailId);

    final Set<ServiceUsageDetail> serviceUsageDetails = rentalDetail.getServiceUsageDetails();

    for (final long serviceUsageDetailId : serviceUsagePayDto.getDetailIds()) {
      final ServiceUsageDetail detail =
          serviceUsageDetails.stream()
              .filter(s -> s.getId().equals(serviceUsageDetailId))
              .findFirst()
              .orElseThrow(RentalDetailMisMatchException::new);

      verifyServiceUsageCanBePaid(detail);

      detail.setIsPaid(true);
    }

    return rentalMapper.toDetailDtoWithBasicRoom(rentalDetailRepository.save(rentalDetail));
  }

  @Override
  @LogAround(jsonOutput = true, jsonInput = true)
  public RentalDetailDto addServiceUsageDetails(
      final long rentalDetailId, final ServiceUsageCreateDto serviceUsageCreateDto) {
    final RentalDetail rentalDetail = getRentalDetailEntityById(rentalDetailId);

    serviceFactory
        .createServiceUsageDetails(rentalDetail, serviceUsageCreateDto.getItems())
        .forEach(rentalDetail::addServiceUsage);

    return rentalMapper.toDetailDtoWithBasicRoom(rentalDetailRepository.save(rentalDetail));
  }

  @Override
  @LogAround(jsonOutput = true, jsonInput = true)
  public InvoiceDto payingRentalDetails(
      final long rentalId, final RentalDetailPayingRequestDto rentalDetailPayingRequestDto) {
    Objects.requireNonNull(rentalDetailPayingRequestDto);
    return payingRentalDetails(getRentalById(rentalId), rentalDetailPayingRequestDto);
  }

  @Override
  @LogAround(jsonInput = true, jsonOutput = true)
  public InvoiceDto payingAllOfRental(
      final long rentalId, final RentalPayingRequestDto rentalPayingRequestDto) {
    Objects.requireNonNull(rentalPayingRequestDto);

    final LocalDateTime checkOutDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    final Rental rental = getRentalById(rentalId);

    final PaymentMethodCode paymentMethodCode = rentalPayingRequestDto.getPaymentMethodCode();

    Invoice newInvoice = invoiceFactory.createInvoice(checkOutDate, rental, paymentMethodCode);

    int invoiceTotal = 0;

    final List<RentalDetail> notPaidDetails = new ArrayList<>();

    for (final RentalDetail detail : rental.getDetails()) {
      if (!detail.getIsPaid()) {
        notPaidDetails.add(detail);
        updateForRentalDetailCheckoutSoon(checkOutDate, detail);
        invoiceTotal +=
            detail.getSubTotal()
                + detail.getNotPaidServiceUsageDetails().stream()
                    .mapToInt(ServiceUsageDetail::getTotal)
                    .sum();
      }
    }

    newInvoice.setTotal(invoiceTotal);

    applyDiscountAmountOfRentalToInvoice(newInvoice, rental);
    applyDepositAmountOfReservationOfRentalToInvoice(newInvoice, rental);

    newInvoice = invoiceRepository.save(newInvoice);

    saveNotPaidRentalDetailAndServiceUsageToInvoice(rental, newInvoice, notPaidDetails);

    return invoiceMapper.toInvoiceDto(invoiceRepository.save(newInvoice));
  }

  @Override
  @LogAround(jsonOutput = true)
  @Transactional(readOnly = true)
  public RentalPayingReviewResponseDto getRentalDetailPayingReview(
      final long rentalId, final List<Long> expectedPaidRentalDetailIds) {
    Objects.requireNonNull(expectedPaidRentalDetailIds);

    final Rental rental = getRentalById(rentalId);

    final LocalDateTime checkOutDate = LocalDateTime.now();

    final List<RentalDetail> currentRentalDetails = rental.getDetails();
    final List<RentalDetailDto> reviewPayingDetails = new ArrayList<>();

    for (final long detailId : expectedPaidRentalDetailIds) {

      final RentalDetail detail = getRentalDetailEntityById(currentRentalDetails, detailId);

      verifyRentalDetailCanBeCheckOut(detail);

      final RentalDetailDto detailDto = rentalMapper.toDetailDtoWithoutRentalInfo(detail);

      updateForRentalDetailCheckoutSoon(checkOutDate, detailDto);

      reviewPayingDetails.add(detailDto);
    }

    final EmployeeDto createdBy = employeeMapper.toEmployeeDto(employeeService.getAuditedLogin());

    final RentalPayingReviewResponseDto response =
        new RentalPayingReviewResponseDto()
            .setCreatedBy(createdBy)
            .setRental(rentalMapper.toBasicDto(rental))
            .setDetails(reviewPayingDetails);

    final boolean hasRemainingDetails =
        isRentalHasRemainingDetails(currentRentalDetails, reviewPayingDetails);

    setRentalDepositUsedMode(rental, response, hasRemainingDetails);
    setRentalDiscountAmountUsedMode(rental, response, hasRemainingDetails);

    return response;
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public RentalPayingReviewResponseDto getRentalDetailPayingReviewForRoomChangeDifferentSuite(
      final long rentalDetailId) {
    final LocalDateTime checkOutDate = LocalDateTime.now();

    final RentalDetail detail = getRentalDetailEntityById(rentalDetailId);

    verifyRentalDetailCanBeCheckOut(detail);

    final RentalDetailDto detailDto = rentalMapper.toDetailDtoWithoutRentalInfo(detail);

    updateForRentalDetailCheckoutSoon(checkOutDate, detailDto);

    return new RentalPayingReviewResponseDto()
        .setCreatedBy(employeeMapper.toEmployeeDto(employeeService.getAuditedLogin()))
        .setRental(rentalMapper.toBasicDto(detail.getRental()))
        .setDetails(List.of(detailDto));
  }

  @Override
  @LogAround(jsonOutput = true)
  @Transactional(readOnly = true)
  public RentalDetailDto getRentalDetailById(final long detailId) {
    return rentalMapper.toDetailDtoWithBasicRoom(getRentalDetailEntityById(detailId));
  }

  @Override
  @LogAround(jsonInput = true, jsonOutput = true)
  @Transactional(isolation = Isolation.SERIALIZABLE)
  public RentalDto saveNewRental(final RentalCreateDto rentalCreateDto) {
    Objects.requireNonNull(rentalCreateDto);

    Rental newRental = rentalFactory.createRental(rentalCreateDto);

    List<RentalDetail> newRentalDetails = new ArrayList<>();

    for (final SuiteMapping suiteMapping : rentalCreateDto.getSuiteMappings()) {

      final Suite suite = roomService.getSuiteById(suiteMapping.getSuiteId());

      for (final RoomMapping roomMapping : suiteMapping.getRoomMappings()) {
        final int roomId = roomMapping.getRoomId();
        final List<CustomerDto> customerDataList = roomMapping.getCustomers();

        verifySuiteMappingIsValid(suite, roomId, customerDataList);

        final Set<Customer> customers =
            customerDataList.stream()
                .map(customerService::saveOccupiedCustomerIfNotExist)
                .collect(Collectors.toSet());

        final String mainCustomerPersonalId = rentalCreateDto.getMainCustomerPersonalId();
        setMainCustomerForRentalIfExist(newRental, customers, mainCustomerPersonalId);

        final RentalDetail newDetail =
            rentalFactory.createRentalDetail(
                newRental.getCheckInAt(),
                newRental.getCheckOutAt(),
                roomId,
                suite.getOriginalPrice(),
                suite.getVat(),
                0,
                0,
                customers);

        newRentalDetails.add(newDetail);
      }
    }

    boolean isMainCustomerNotFound = newRental.getOwner() == null;
    if (isMainCustomerNotFound) {
      throw new RentalSaveIllegalException("Main customer not found");
    }

    newRentalDetails.forEach(newRental::addDetail);

    newRental = rentalRepository.save(newRental);

    for (final RentalDetail detail : newRental.getDetails()) {
      detail.setPromotions(
          promotionService.getSuitePromotionsFromDate(
              detail.getRoom().getSuiteId(),
              detail.getCheckInAt().toLocalDate(),
              detail.getCheckOutAt().toLocalDate()));
      roomService.clearSuiteCache(detail.getRoom().getSuite());
    }
    return rentalMapper.toDto(newRental);
  }

  @Override
  @LogAround(jsonOutput = true)
  @Transactional(readOnly = true)
  public RentabilityRoomsMappingDto getRentabilityRoomMappingFromReservation(
      final long reservationId) {
    final Reservation reservation = reservationService.getReservationById(reservationId);

    verifyReservationCanSaveToRental(reservation);

    final RentabilityRoomsMappingDto rentabilityRoomsMappingDto = new RentabilityRoomsMappingDto();
    final Map<Integer, List<RoomDto>> suiteIdMapToRooms = new LinkedHashMap<>();

    for (final ReservationDetail detail : reservation.getDetails()) {
      final Suite suite = detail.getSuite();
      final List<Room> rooms = roomRepository.findRoomsCanRentBySuiteId(suite.getId());

      if (rooms.size() < detail.getRoomNum()) {
        throw new RentalRoomMappingNotResolvedException(
            String.format(
                "Number of room is not enough to rent [actual: %d, desired: %d]",
                rooms.size(), detail.getRoomNum()));
      }

      suiteIdMapToRooms.put(suite.getId(), roomMapper.toRoomBasicDtoWithoutSuite(rooms));
    }

    rentabilityRoomsMappingDto.setRoomMapping(suiteIdMapToRooms);
    return rentabilityRoomsMappingDto;
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public Optional<LocalDate> getRentalDetailMaximumCheckOutDate(final long rentalDetailId) {
    if (!rentalDetailRepository.existsById(rentalDetailId)) {
      throw new RentalDetailNotFoundException(rentalDetailId);
    }
    return rentalDetailRepository.getMaximumCheckOutDate(
        rentalDetailId, RENTAL_DETAIL_MAXIMUM_CHECKOUT_DAY_THRESHOLD);
  }

  @Override
  @LogAround(jsonInput = true, jsonOutput = true)
  public RentalDetailDto updateRentalDetail(
      final long rentalDetailId, final RentalDetailUpdateDto rentalDetailUpdateDto) {
    Objects.requireNonNull(rentalDetailUpdateDto);

    final RentalDetail detail = getRentalDetailEntityById(rentalDetailId);
    if (detail.getIsPaid()) {
      throw new RentalDetailUpdateIllegalException("Paid rental detail cannot be updated");
    }

    boolean updated = false;

    final LocalDate maximumCheckOutDate =
        rentalDetailRepository
            .getMaximumCheckOutDate(rentalDetailId, RENTAL_DETAIL_MAXIMUM_CHECKOUT_DAY_THRESHOLD)
            .orElseThrow();

    final LocalDateTime updateCheckOutAt = rentalDetailUpdateDto.getCheckOutAt();
    if (updateCheckOutAt != null) {
      if (updateCheckOutAt.toLocalDate().isAfter(maximumCheckOutDate)) {
        throw new RentalDetailUpdateIllegalException(
            "Cannot update checkout date over maximum value");
      }

      detail.setCheckOutAt(updateCheckOutAt);
      updated = true;
    }

    final Integer updateDiscountAmount = rentalDetailUpdateDto.getDiscountAmount();
    if (updateDiscountAmount != null) {
      detail.setDiscountAmount(updateDiscountAmount);
      updated = true;
    }

    final Integer updateExtraAmount = rentalDetailUpdateDto.getExtraAmount();
    if (updateExtraAmount != null) {
      detail.setExtraAmount(updateExtraAmount);
      updated = true;
    }

    if (updated) {
      updatePromotionWithTotalForEarlierCheckOutRentalDetail(detail);
    }

    return rentalMapper.toDetailDtoWithBasicRoom(rentalDetailRepository.save(detail));
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public List<RoomDto> getRentalDetailRoomChangeSuggestions(
      final long rentalDetailId, boolean isSameSuite) {
    return rentalDetailRepository.getRentalDetailRoomChangeSuggestions(rentalDetailId, isSameSuite);
  }

  @Override
  @LogAround
  public RentalDetailDto changeRoomSameSuiteForRentalDetail(
      final long rentalDetailId, final int roomId) {
    final RentalDetail rentalDetail = getRentalDetailEntityByIdWithPromotions(rentalDetailId);

    validateRoomChangeAbility(rentalDetail);

    final List<RoomDto> roomSuggestions =
        getRentalDetailRoomChangeSuggestions(rentalDetailId, true);

    if (roomSuggestions.stream().noneMatch(room -> room.getId() == roomId)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "New room is not permited to change");
    }

    roomService.updateStatusOfRoom(rentalDetail.getRoomId(), RoomStatusCode.CLEANY);
    rentalDetail.setRoom(roomService.getRoomById(roomId));
    roomService.updateStatusOfRoom(roomId, RoomStatusCode.RENTED);
    return rentalMapper.toDetailDtoWithBasicRoom(rentalDetailRepository.save(rentalDetail));
  }

  @Override
  @LogAround
  public RentalDetailDto changeRoomDifferentSuiteForRentalDetail(long rentalDetailId, int roomId) {
    final RentalDetail rentalDetail = getRentalDetailEntityByIdWithPromotions(rentalDetailId);
    final LocalDateTime checkoutAt = rentalDetail.getCheckOutAt();
    validateRoomChangeAbility(rentalDetail);

    final List<RoomDto> roomSuggestions =
        getRentalDetailRoomChangeSuggestions(rentalDetailId, false);

    final RoomDto destRoom =
        roomSuggestions.stream()
            .filter(room -> room.getId() == roomId)
            .findFirst()
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "New room is not permited to change"));

    final Rental rental = rentalDetail.getRental();

    final RentalDetail newRentalDetail =
        rentalFactory.createRentalDetail(
            LocalDateTime.now(),
            checkoutAt,
            roomId,
            destRoom.getSuite().getOriginalPrice(),
            10,
            0,
            0,
            new HashSet<>(rentalDetail.getCustomers()));

    rental.addDetail(newRentalDetail);

    payingRentalDetails(
        rental,
        new RentalDetailPayingRequestDto()
            .setRentalDetailIds(List.of(rentalDetailId))
            .setInvoiceType(InvoiceType.NO_VAT)
            .setPaymentMethodCode(PaymentMethodCode.CASH));

    rentalRepository.save(rental);
    return rentalMapper.toDetailDtoWithBasicRoom(rentalDetail);
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public boolean isCustomersRented(List<String> personalIds) {
    return rentalDetailRepository.isCustomersRented(personalIds);
  }

  @Override
  public RentalDetail setRentalDetailPromotions(final RentalDetail rentalDetail) {
    Objects.requireNonNull(rentalDetail);
    log.debug("Set promotions for rental detail [id: '{}']", rentalDetail.getId());

    return rentalDetail.setPromotions(
        promotionService.getSuitePromotionsFromDate(
            rentalDetail.getRoom().getSuiteId(),
            rentalDetail.getCheckInAt().toLocalDate(),
            rentalDetail.getCheckOutAt().toLocalDate()));
  }

  private InvoiceDto payingRentalDetails(
      final Rental rental, final RentalDetailPayingRequestDto rentalDetailPayingRequestDto) {
    final List<RentalDetail> rentalDetails = rental.getDetails();

    final List<Long> selectedRentalDetailIds = rentalDetailPayingRequestDto.getRentalDetailIds();

    final PaymentMethodCode paymentMethodCode = rentalDetailPayingRequestDto.getPaymentMethodCode();

    final InvoiceType invoiceType = rentalDetailPayingRequestDto.getInvoiceType();
    final RentalDetailPayingRequestDto.VATCustomerOption vatCustomerOption =
        rentalDetailPayingRequestDto.getVatCustomerOption();

    Invoice newInvoice;

    final LocalDateTime checkOutDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    if (invoiceType.equals(InvoiceType.NO_VAT)) {
      newInvoice = invoiceFactory.createInvoice(checkOutDate, rental, paymentMethodCode);
    } else {
      if (vatCustomerOption == null) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "VAT customer option should not be null");
      }
      newInvoice =
          invoiceFactory.createVATInvoice(
              checkOutDate,
              rental,
              paymentMethodCode,
              vatCustomerOption.getCustomerName(),
              vatCustomerOption.getTaxCode());
      // Update tax code for customer
      final Customer owner = rental.getOwner();
      if (owner.getFullName().equals(vatCustomerOption.getCustomerName())
          && owner.getTaxCode() == null) {
        owner.setTaxCode(vatCustomerOption.getTaxCode());
      }
    }

    final List<RentalDetail> selectedRentalDetails = new ArrayList<>();

    int invoiceTotal = 0;

    for (final long selectedId : selectedRentalDetailIds) {
      final RentalDetail detail = getRentalDetailEntityById(rentalDetails, selectedId);

      verifyRentalDetailCanBeCheckOut(detail);
      selectedRentalDetails.add(detail);

      updateForRentalDetailCheckoutSoon(checkOutDate, detail);

      invoiceTotal +=
          detail.getSubTotal()
              + detail.getNotPaidServiceUsageDetails().stream()
                  .mapToInt(ServiceUsageDetail::getTotal)
                  .sum();
    }

    newInvoice.setTotal(invoiceTotal);

    final RentalPaymentCreateDto paymentCreateDto = rentalDetailPayingRequestDto.getPayment();
    if (paymentCreateDto != null) {
      final RentalPayment payment =
          rentalFactory.createRentalPayment(checkOutDate, paymentCreateDto);
      rental.addPayment(payment);
      newInvoice.setTotal(newInvoice.getTotal() + paymentCreateDto.getMoney());
    }

    final boolean isDiscountAmountUsed = rentalDetailPayingRequestDto.getRentalDiscountUsed();
    if (isDiscountAmountUsed) {
      applyDiscountAmountOfRentalToInvoice(newInvoice, rental);
    }

    final boolean isDepositAmountUsed = rentalDetailPayingRequestDto.getDepositUsed();
    if (isDepositAmountUsed) {
      applyDepositAmountOfReservationOfRentalToInvoice(newInvoice, rental);
    }

    if (newInvoice.getTotal() < 0) {
      throw new IllegalStateException("No vat invoice has invalid total");
    }

    newInvoice = invoiceRepository.save(newInvoice);

    saveNotPaidRentalDetailAndServiceUsageToInvoice(rental, newInvoice, selectedRentalDetails);

    return invoiceMapper.toInvoiceDto(invoiceRepository.save(newInvoice));
  }

  private void validateRoomChangeAbility(final RentalDetail rentalDetail) {
    if (rentalDetail.getIsPaid()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Cannot change room for the paid rental detail");
    }
  }

  private void saveNotPaidRentalDetailAndServiceUsageToInvoice(
      final Rental rental, final Invoice newInvoice, final List<RentalDetail> notPaidDetails) {
    log.debug("Add not paid rental details and services to the invoice");
    for (final RentalDetail detail : notPaidDetails) {
      newInvoice.addRentalDetail(detail);
      detail.getNotPaidServiceUsageDetails().forEach(newInvoice::addServiceUsage);
    }
    updateStatusOfRentalAfterCheckOutDetail(rental);
  }

  private void verifyServiceUsageCanBePaid(final ServiceUsageDetail detail) {
    if (detail.getIsPaid()) {
      throw new ServiceUsagePayingErrorException();
    }
  }

  private void setMainCustomerForRentalIfExist(
      final Rental newRental, final Set<Customer> customers, final String mainCustomerPersonalId) {
    customers.stream()
        .filter(c -> c.getPersonalId().equalsIgnoreCase(mainCustomerPersonalId))
        .findAny()
        .ifPresent(
            customer -> {
              log.debug("Set the owner to a new rental ({})", customer);
              newRental.setOwner(customer);
            });
  }

  private void verifySuiteMappingIsValid(
      final Suite suite, final int roomId, final List<CustomerDto> customerDataList) {
    final Room room = roomService.getRoomById(roomId);
    if (!room.getSuite().equals(suite)) {
      throw new RequestParamInvalidException("Room id is not matched", "roomId", "notMatched");
    }

    final int suiteMaxOccupation = suite.getSuiteStyle().getMaxOccupation();
    final int numCustomer = customerDataList.size();
    if (numCustomer > suiteMaxOccupation) {
      throw new RequestParamInvalidException(
          "Room occupation is not correct", "customers", "occupationError");
    }
  }

  private void applyDepositAmountOfReservationOfRentalToInvoice(
      final Invoice invoice, final Rental rental) {
    final Reservation reservation = rental.getReservation();
    final boolean isRentalHasReservation = reservation != null;

    if (isRentalHasReservation) {
      final boolean isDepositAmountPaidBefore =
          invoiceRepository.existsByRentalIdAndDepositUsedIsTrue(rental.getId());

      if (isDepositAmountPaidBefore) return;

      final int currentInvoiceTotal = invoice.getTotal();
      final int reservationDepositAmount = reservation.getDepositAmount();

      if (currentInvoiceTotal < reservationDepositAmount) {
        throw new IllegalArgumentException(
            String.format(
                "Invoice total must be greater than rental's reservation deposit amount [invoice total: '%d', deposit: '%d']",
                currentInvoiceTotal, reservationDepositAmount));
      }

      invoice.setTotal(currentInvoiceTotal - reservationDepositAmount);
      invoice.setDepositUsed(true);
      log.debug("Applying reservation's deposit amount to an invoice");
    }
  }

  private void applyDiscountAmountOfRentalToInvoice(final Invoice invoice, final Rental rental) {
    final int rentalDiscountAmount = rental.getDiscountAmount();
    final boolean isDiscountAmountAvailable = rentalDiscountAmount > 0;

    if (isDiscountAmountAvailable) {
      boolean isDiscountAmountUsedBefore =
          invoiceRepository.existsByRentalIdAndRentalDiscountUsedIsTrue(rental.getId());

      if (isDiscountAmountUsedBefore) return;

      final int currentInvoiceTotal = invoice.getTotal();
      if (currentInvoiceTotal < rentalDiscountAmount) {
        throw new IllegalArgumentException(
            String.format(
                "Invoice total must be greater than rental discount amount [invoice total: '%d', rental discount: '%d']",
                currentInvoiceTotal, rentalDiscountAmount));
      }
      invoice.setTotal(currentInvoiceTotal - rentalDiscountAmount);
      invoice.setRentalDiscountUsed(true);
      log.debug("Applying rental's discount amount to an invoice");
    }
  }

  private void verifyRentalDetailCanBeCheckOut(final RentalDetail detail) {
    if (detail.getIsPaid()) {
      throw new RentalDetailPayingIllegalException();
    }
  }

  private void updateForRentalDetailCheckoutSoon(
      final LocalDateTime expectedCheckOutAt, final RentalDetailUpdatable detail) {
    final boolean isDetailCheckOutEarlier = expectedCheckOutAt.isBefore(detail.getCheckOutAt());
    if (isDetailCheckOutEarlier) {
      log.debug(
          "Update promotion and total for detail checkout soon [from '{}' to '{}']",
          detail.getCheckOutAt(),
          expectedCheckOutAt);
      detail.setCheckOutAt(expectedCheckOutAt);
      updatePromotionWithTotalForEarlierCheckOutRentalDetail(detail);
    }
  }

  private boolean isRentalHasRemainingDetails(
      final List<RentalDetail> currentRentalDetails,
      final List<RentalDetailDto> reviewPayingDetails) {
    final int numPaidDetail =
        (int) currentRentalDetails.stream().filter(RentalDetail::getIsPaid).count();
    final int totalDetail = currentRentalDetails.size();
    return (numPaidDetail + reviewPayingDetails.size()) < totalDetail;
  }

  private void setRentalDepositUsedMode(
      final Rental rental,
      final RentalPayingReviewResponseDto response,
      final boolean hasRemainingDetails) {
    response.setDepositUsedMode(UNABLE);

    final int depositAmount =
        rental.getReservation() != null ? rental.getReservation().getDepositAmount() : 0;

    final boolean isRentalHasDepositAmount = depositAmount > 0;
    if (isRentalHasDepositAmount) {
      final boolean isDepositAmountNotPaidBefore =
          !invoiceRepository.existsByRentalIdAndDepositUsedIsTrue(rental.getId());

      if (isDepositAmountNotPaidBefore) {
        response.setDepositAmount(depositAmount);
        response.setDepositUsedMode(hasRemainingDetails ? ABLE : FORCE);
      }
    }
  }

  private void setRentalDiscountAmountUsedMode(
      final Rental rental,
      final RentalPayingReviewResponseDto response,
      final boolean hasRemainingDetails) {
    response.setRentalDiscountUsedMode(UNABLE);

    final boolean rentalHasDiscountAmount = rental.getDiscountAmount() > 0;

    if (rentalHasDiscountAmount) {
      boolean isDiscountAmountNotUsedBefore =
          !invoiceRepository.existsByRentalIdAndRentalDiscountUsedIsTrue(rental.getId());

      if (isDiscountAmountNotUsedBefore) {
        response.setRentalDiscountUsedMode(hasRemainingDetails ? ABLE : FORCE);
      }
    }
  }

  private RentalDetail getRentalDetailEntityById(
      final List<RentalDetail> currentRentalDetails, final long detailId) {
    return currentRentalDetails.stream()
        .filter(d -> d.getId().equals(detailId))
        .findAny()
        .orElseThrow(RentalDetailMisMatchException::new);
  }

  private void updatePromotionWithTotalForEarlierCheckOutRentalDetail(
      final RentalDetailUpdatable rentailDetail) {
    rentailDetail.setPromotions(
        promotionService.getSuitePromotionsFromDate(
            rentailDetail.getSuiteId(),
            rentailDetail.getCheckInAt().toLocalDate(),
            rentailDetail.getCheckOutAt().toLocalDate()));

    rentailDetail.updateSubTotal();
    log.debug("Update rental detail sub total '{}'", rentailDetail.getSubTotal());
  }

  private RentalDetail getRentalDetailEntityById(final long rentalDetailId) {
    return rentalDetailRepository
        .findById(rentalDetailId)
        .map(this::setRentalDetailPromotions)
        .orElseThrow(() -> new RentalDetailNotFoundException(rentalDetailId));
  }

  private RentalDetail getRentalDetailEntityByIdWithPromotions(final long rentalDetailId) {
    return rentalDetailRepository
        .findById(rentalDetailId)
        .orElseThrow(() -> new RentalDetailNotFoundException(rentalDetailId));
  }

  private void updateStatusOfRentalAfterCheckOutDetail(final Rental rental) {
    final List<RentalDetail> details = rental.getDetails();

    final boolean isAllDetailsPaid = details.stream().allMatch(RentalDetail::getIsPaid);
    final boolean isAnyDetailsNotPaid = details.stream().anyMatch(RentalDetail::getIsPaid);

    if (isAllDetailsPaid) {
      rental.setStatus(commonService.getRentalStatusByCode(FULL_CHECKOUT));
      log.debug("Update rental status to 'FULL_CHECKOUT'");
    } else if (isAnyDetailsNotPaid) {
      rental.setStatus(commonService.getRentalStatusByCode(HALF_CHECKOUT));
      log.debug("Update rental status to 'HALF_CHECKOUT'");
    }
  }

  private void verifyReservationCanSaveToRental(final Reservation reservation) {
    final LocalDate nowDate = LocalDate.now();
    final LocalDate checkInAt = reservation.getCheckInAt();
    final LocalDate checkOutAt = reservation.getCheckOutAt();

    boolean isNowDateBeforeCheckInAt = nowDate.isBefore(checkInAt);
    boolean isNowDateAfterCheckOutAt = nowDate.isAfter(checkOutAt);

    if (isNowDateBeforeCheckInAt || isNowDateAfterCheckOutAt) {
      throw new BadRequestException(
          String.format(
              "Cannot save reservation to rental due to invalid date check [checkInAt: '%s', checkOutAt: '%s']",
              checkInAt, checkOutAt));
    }
  }
}