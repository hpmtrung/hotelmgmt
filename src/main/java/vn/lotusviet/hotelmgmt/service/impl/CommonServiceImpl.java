package vn.lotusviet.hotelmgmt.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.exception.*;
import vn.lotusviet.hotelmgmt.exception.entity.RentalStatusNotFoundException;
import vn.lotusviet.hotelmgmt.exception.entity.ReservationStatusNotFoundException;
import vn.lotusviet.hotelmgmt.exception.entity.RoomStatusCodeNotFoundException;
import vn.lotusviet.hotelmgmt.model.dto.system.CommonDataDto;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethod;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatus;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatusCode;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatus;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatusCode;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatus;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatusCode;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteStyle;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteType;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceType;
import vn.lotusviet.hotelmgmt.repository.checkin.ReservationStatusRepository;
import vn.lotusviet.hotelmgmt.repository.checkout.RentalStatusRepository;
import vn.lotusviet.hotelmgmt.repository.checkout.ServiceRepository;
import vn.lotusviet.hotelmgmt.repository.checkout.ServiceTypeRepository;
import vn.lotusviet.hotelmgmt.repository.misc.PaymentMethodRepository;
import vn.lotusviet.hotelmgmt.repository.person.DepartmentRepository;
import vn.lotusviet.hotelmgmt.repository.room.AmenityRepository;
import vn.lotusviet.hotelmgmt.repository.room.RoomStatusRepository;
import vn.lotusviet.hotelmgmt.repository.room.SuiteStyleRepository;
import vn.lotusviet.hotelmgmt.repository.room.SuiteTypeRepository;
import vn.lotusviet.hotelmgmt.service.CommonService;
import vn.lotusviet.hotelmgmt.service.mapper.AmenityMapper;
import vn.lotusviet.hotelmgmt.service.mapper.DepartmentMapper;
import vn.lotusviet.hotelmgmt.service.mapper.ServiceMapper;

@Service
@Transactional(readOnly = true)
public class CommonServiceImpl implements CommonService {

  private final AmenityMapper amenityMapper;
  private final DepartmentMapper departmentMapper;
  private final ServiceMapper serviceMapper;
  private final AmenityRepository amenityRepository;
  private final DepartmentRepository departmentRepository;
  private final ReservationStatusRepository reservationStatusRepository;
  private final ServiceTypeRepository serviceTypeRepository;
  private final PaymentMethodRepository paymentMethodRepository;
  private final RoomStatusRepository roomStatusRepository;
  private final SuiteStyleRepository suiteStyleRepository;
  private final SuiteTypeRepository suiteTypeRepository;
  private final RentalStatusRepository rentalStatusRepository;
  private final ServiceRepository serviceRepository;

  public CommonServiceImpl(
      AmenityMapper amenityMapper,
      DepartmentMapper departmentMapper,
      ServiceMapper serviceMapper,
      AmenityRepository amenityRepository,
      DepartmentRepository departmentRepository,
      ReservationStatusRepository reservationStatusRepository,
      ServiceTypeRepository serviceTypeRepository,
      PaymentMethodRepository paymentMethodRepository,
      RoomStatusRepository roomStatusRepository,
      SuiteStyleRepository suiteStyleRepository,
      SuiteTypeRepository suiteTypeRepository,
      RentalStatusRepository rentalStatusRepository,
      ServiceRepository serviceRepository) {
    this.amenityMapper = amenityMapper;
    this.departmentMapper = departmentMapper;
    this.serviceMapper = serviceMapper;
    this.amenityRepository = amenityRepository;
    this.departmentRepository = departmentRepository;
    this.reservationStatusRepository = reservationStatusRepository;
    this.serviceTypeRepository = serviceTypeRepository;
    this.paymentMethodRepository = paymentMethodRepository;
    this.roomStatusRepository = roomStatusRepository;
    this.suiteStyleRepository = suiteStyleRepository;
    this.suiteTypeRepository = suiteTypeRepository;
    this.rentalStatusRepository = rentalStatusRepository;
    this.serviceRepository = serviceRepository;
  }

  @Override
  @Cacheable(value = COMMON_DATA_APP_CACHE_NAME, key = "'customer'")
  public CommonDataDto getCustomerAppCommonData() {
    return new CommonDataDto()
        .setAmenities(amenityMapper.toAmentityDto(amenityRepository.findAll()))
        .setReservationStatuses(reservationStatusRepository.findAll())
        .setPaymentMethods(paymentMethodRepository.findAll())
        .setRoomStatuses(roomStatusRepository.findAll())
        .setSuiteStyles(suiteStyleRepository.findAll())
        .setSuiteTypes(suiteTypeRepository.findAll());
  }

  @Override
  @Cacheable(value = COMMON_DATA_APP_CACHE_NAME, key = "'portal'")
  public CommonDataDto getPortalAppCommonData() {
    return new CommonDataDto()
        .setAmenities(amenityMapper.toAmentityDto(amenityRepository.findAll()))
        .setDepartments(departmentMapper.toDepartmentDto(departmentRepository.findAll()))
        .setReservationStatuses(reservationStatusRepository.findAll())
        .setRentalStatuses(rentalStatusRepository.findAll())
        .setServiceTypes(serviceMapper.toTypeDto(serviceTypeRepository.findAll()))
        .setPaymentMethods(paymentMethodRepository.findAll())
        .setRoomStatuses(roomStatusRepository.findAll())
        .setSuiteStyles(suiteStyleRepository.findAll())
        .setSuiteTypes(suiteTypeRepository.findAll());
  }

  @Override
  public PaymentMethod getPaymentMethodByCode(PaymentMethodCode paymentMethodCode) {
    return paymentMethodRepository
        .findByCode(paymentMethodCode)
        .orElseThrow(() -> new PaymentMethodNotFoundException(paymentMethodCode));
  }

  @Override
  @Transactional(readOnly = true)
  public ReservationStatus getReservationStatusByCode(ReservationStatusCode statusCode) {
    return reservationStatusRepository
        .findByCode(statusCode)
        .orElseThrow(() -> new ReservationStatusNotFoundException(statusCode));
  }

  @Override
  @Transactional(readOnly = true)
  public RentalStatus getRentalStatusByCode(RentalStatusCode statusCode) {
    return rentalStatusRepository
        .findByCode(statusCode)
        .orElseThrow(() -> new RentalStatusNotFoundException(statusCode));
  }

  @Override
  @Transactional(readOnly = true)
  public RoomStatus getRoomStatusByCode(RoomStatusCode roomStatusCode) {
    return roomStatusRepository
        .findByCode(roomStatusCode)
        .orElseThrow(() -> new RoomStatusCodeNotFoundException(roomStatusCode));
  }

  @Override
  @Transactional(readOnly = true)
  public SuiteStyle getSuiteStyleById(int id) {
    return suiteStyleRepository.findById(id).orElseThrow(() -> new SuiteStyleNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public SuiteType getSuiteTypeById(int id) {
    return suiteTypeRepository.findById(id).orElseThrow(() -> new SuiteTypeNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public ServiceType getServiceTypeById(int id) {
    return serviceTypeRepository
        .findById(id)
        .orElseThrow(() -> new ServiceTypeNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public vn.lotusviet.hotelmgmt.model.entity.service.Service getServiceById(int serviceId) {
    return serviceRepository
        .findById(serviceId)
        .orElseThrow(() -> new ServiceNotFoundException(serviceId));
  }

  @Override
  @CacheEvict(value = COMMON_DATA_APP_CACHE_NAME, allEntries = true)
  public void clearCache() {
    // Clear cache via annotation usage
  }
}