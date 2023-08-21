package vn.lotusviet.hotelmgmt.model.dto.rental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationDto;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatusCode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RentalDto {

  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime checkInAt;
  private LocalDateTime checkOutAt;
  private Integer discountAmount;
  private ReservationDto reservation;
  private EmployeeDto createdBy;
  private CustomerDto owner;
  private RentalStatusCode status;
  private List<RentalDetailDto> details;
  private List<RentalPaymentDto> rentalPayments;

  public List<RentalPaymentDto> getRentalPayments() {
    return rentalPayments;
  }

  public RentalDto setRentalPayments(List<RentalPaymentDto> rentalPayments) {
    this.rentalPayments = rentalPayments;
    return this;
  }

  @JsonProperty("nightCount")
  public Integer getNightCount() {
    if (checkInAt == null || checkOutAt == null) return null;
    return Math.max(
        (int) (checkInAt.toLocalDate().until(checkOutAt.toLocalDate(), ChronoUnit.DAYS)), 1);
  }

  public RentalStatusCode getStatus() {
    return status;
  }

  public RentalDto setStatus(RentalStatusCode status) {
    this.status = status;
    return this;
  }

  public Long getId() {
    return id;
  }

  public RentalDto setId(final Long id) {
    this.id = id;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public RentalDto setCreatedAt(final LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getCheckInAt() {
    return checkInAt;
  }

  public RentalDto setCheckInAt(final LocalDateTime checkInAt) {
    this.checkInAt = checkInAt;
    return this;
  }

  public LocalDateTime getCheckOutAt() {
    return checkOutAt;
  }

  public RentalDto setCheckOutAt(final LocalDateTime checkOutAt) {
    this.checkOutAt = checkOutAt;
    return this;
  }

  public Integer getDiscountAmount() {
    return discountAmount;
  }

  public RentalDto setDiscountAmount(final Integer discountAmount) {
    this.discountAmount = discountAmount;
    return this;
  }

  public ReservationDto getReservation() {
    return reservation;
  }

  public RentalDto setReservation(final ReservationDto reservation) {
    this.reservation = reservation;
    return this;
  }

  public EmployeeDto getCreatedBy() {
    return createdBy;
  }

  public RentalDto setCreatedBy(final EmployeeDto createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public CustomerDto getOwner() {
    return owner;
  }

  public RentalDto setOwner(final CustomerDto owner) {
    this.owner = owner;
    return this;
  }

  public List<RentalDetailDto> getPaidDetails() {
    return details.stream().filter(RentalDetailDto::getIsPaid).collect(Collectors.toList());
  }

  public List<RentalDetailDto> getNotPaidDetails() {
    return details.stream().filter(d -> !d.getIsPaid()).collect(Collectors.toList());
  }

  public RentalDto setDetails(final List<RentalDetailDto> details) {
    this.details = details;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RentalDto)) return false;
    RentalDto rentalDto = (RentalDto) o;
    return Objects.equals(getId(), rentalDto.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "RentalDto{"
        + "id="
        + id
        + ", createdAt="
        + createdAt
        + ", checkInAt="
        + checkInAt
        + ", checkOutAt="
        + checkOutAt
        + ", discount="
        + discountAmount
        + ", reservation="
        + reservation
        + ", employee="
        + createdBy
        + ", customer="
        + owner
        + ", rentalDetails="
        + details
        + '}';
  }
}