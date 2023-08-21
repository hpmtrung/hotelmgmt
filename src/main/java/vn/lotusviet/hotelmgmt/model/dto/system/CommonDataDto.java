package vn.lotusviet.hotelmgmt.model.dto.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.lotusviet.hotelmgmt.model.dto.person.DepartmentDto;
import vn.lotusviet.hotelmgmt.model.dto.room.AmenityDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceTypeDto;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethod;
import vn.lotusviet.hotelmgmt.model.entity.person.AuthorityName;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatus;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatus;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatus;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteStyle;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteType;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonDataDto {

  private List<AmenityDto> amenities;
  private List<DepartmentDto> departments;
  private List<ReservationStatus> reservationStatuses;
  private List<RentalStatus> rentalStatuses;
  private List<ServiceTypeDto> serviceTypes;
  private List<PaymentMethod> paymentMethods;
  private List<RoomStatus> roomStatuses;
  private List<SuiteStyle> suiteStyles;
  private List<SuiteType> suiteTypes;
  private List<AuthorityName> employeeRoles;

  public List<RentalStatus> getRentalStatuses() {
    return rentalStatuses;
  }

  public CommonDataDto setRentalStatuses(List<RentalStatus> rentalStatuses) {
    this.rentalStatuses = rentalStatuses;
    return this;
  }

  public List<AuthorityName> getEmployeeRoles() {
    return employeeRoles;
  }

  public CommonDataDto setEmployeeRoles(List<AuthorityName> employeeRoles) {
    this.employeeRoles = employeeRoles;
    return this;
  }

  public List<AmenityDto> getAmenities() {
    return amenities;
  }

  public CommonDataDto setAmenities(final List<AmenityDto> amenities) {
    this.amenities = amenities;
    return this;
  }

  public List<DepartmentDto> getDepartments() {
    return departments;
  }

  public CommonDataDto setDepartments(final List<DepartmentDto> departments) {
    this.departments = departments;
    return this;
  }

  public List<ReservationStatus> getReservationStatuses() {
    return reservationStatuses;
  }

  public CommonDataDto setReservationStatuses(final List<ReservationStatus> reservationStatuses) {
    this.reservationStatuses = reservationStatuses;
    return this;
  }

  public List<ServiceTypeDto> getServiceTypes() {
    return serviceTypes;
  }

  public CommonDataDto setServiceTypes(final List<ServiceTypeDto> serviceTypes) {
    this.serviceTypes = serviceTypes;
    return this;
  }

  public List<PaymentMethod> getPaymentMethods() {
    return paymentMethods;
  }

  public CommonDataDto setPaymentMethods(final List<PaymentMethod> paymentMethods) {
    this.paymentMethods = paymentMethods;
    return this;
  }

  public List<RoomStatus> getRoomStatuses() {
    return roomStatuses;
  }

  public CommonDataDto setRoomStatuses(final List<RoomStatus> roomStatuses) {
    this.roomStatuses = roomStatuses;
    return this;
  }

  public List<SuiteStyle> getSuiteStyles() {
    return suiteStyles;
  }

  public CommonDataDto setSuiteStyles(final List<SuiteStyle> suiteStyles) {
    this.suiteStyles = suiteStyles;
    return this;
  }

  public List<SuiteType> getSuiteTypes() {
    return suiteTypes;
  }

  public CommonDataDto setSuiteTypes(final List<SuiteType> suiteTypes) {
    this.suiteTypes = suiteTypes;
    return this;
  }
}