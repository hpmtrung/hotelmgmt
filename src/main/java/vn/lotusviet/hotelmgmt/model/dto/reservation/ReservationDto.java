package vn.lotusviet.hotelmgmt.model.dto.reservation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatusCode;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatusCode.TEMPORARY;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationDto {

  private Long id;
  private Long rentalId;
  private CustomerDto owner;
  private Instant createdAt;
  private LocalDate checkInAt;
  private LocalDate checkOutAt;
  private Integer depositAmount;
  private Integer depositPercent;
  private String specialRequirements;
  private PaymentMethodCode paymentMethodCode;
  private Integer fee;
  private ReservationStatusCode statusCode;
  private EmployeeDto createdBy;
  private List<ReservationDetailDto> details;
  private Integer total;
  private Integer timeElapsedMins;
  private Integer refund;

  public Integer getRefund() {
    return refund;
  }

  public ReservationDto setRefund(Integer refund) {
    this.refund = refund;
    return this;
  }

  public Long getRentalId() {
    return rentalId;
  }

  public void setRentalId(Long rentalId) {
    this.rentalId = rentalId;
  }

  public Integer getTotal() {
    return this.total;
  }

  public ReservationDto setTotal(Integer total) {
    this.total = total;
    return this;
  }

  public Integer getDepositPercent() {
    return depositPercent;
  }

  public void setDepositPercent(Integer depositPercent) {
    this.depositPercent = depositPercent;
  }

  @JsonProperty("originalTotal")
  public Integer getOriginalTotal() {
    if (this.details == null) return null;
    return details.stream().mapToInt(ReservationDetailDto::getOriginalSubTotal).sum();
  }

  @JsonProperty("discountTotal")
  public Integer getDiscountTotal() {
    if (this.details == null) return null;
    return details.stream().mapToInt(ReservationDetailDto::getTotalDiscountFromPromotion).sum();
  }

  @JsonProperty("nightCount")
  public Integer getNightCount() {
    if (checkInAt == null || checkOutAt == null) return null;
    return Math.max((int) (checkInAt.until(checkOutAt, ChronoUnit.DAYS)), 1);
  }

  public Integer getTimeElapsedMins() {
    return timeElapsedMins;
  }

  public ReservationDto setTimeElapsedMins(Integer timeElapsedMins) {
    this.timeElapsedMins = timeElapsedMins;
    return this;
  }

  @JsonProperty("remaining")
  public Integer getRemaining() {
    if (TEMPORARY.equals(statusCode)) return this.total;
    Integer amount = getDepositAmount();
    return amount != null ? this.total - amount : null;
  }

  public PaymentMethodCode getPaymentMethodCode() {
    return paymentMethodCode;
  }

  public ReservationDto setPaymentMethodCode(final PaymentMethodCode paymentMethodCode) {
    this.paymentMethodCode = paymentMethodCode;
    return this;
  }

  public Long getId() {
    return id;
  }

  public ReservationDto setId(final Long id) {
    this.id = id;
    return this;
  }

  public CustomerDto getOwner() {
    return owner;
  }

  public ReservationDto setOwner(final CustomerDto owner) {
    this.owner = owner;
    return this;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public ReservationDto setCreatedAt(final Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public String getSpecialRequirements() {
    return specialRequirements;
  }

  public ReservationDto setSpecialRequirements(final String specialRequirements) {
    this.specialRequirements = specialRequirements;
    return this;
  }

  public Integer getDepositAmount() {
    return depositAmount;
  }

  public ReservationDto setDepositAmount(final Integer depositAmount) {
    this.depositAmount = depositAmount;
    return this;
  }

  public LocalDate getCheckInAt() {
    return checkInAt;
  }

  public ReservationDto setCheckInAt(final LocalDate checkInAt) {
    this.checkInAt = checkInAt;
    return this;
  }

  public LocalDate getCheckOutAt() {
    return checkOutAt;
  }

  public ReservationDto setCheckOutAt(final LocalDate checkOutAt) {
    this.checkOutAt = checkOutAt;
    return this;
  }

  public Integer getFee() {
    return fee;
  }

  public ReservationDto setFee(final Integer fee) {
    this.fee = fee;
    return this;
  }

  public ReservationStatusCode getStatusCode() {
    return statusCode;
  }

  public ReservationDto setStatusCode(final ReservationStatusCode statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  public EmployeeDto getCreatedBy() {
    return createdBy;
  }

  public ReservationDto setCreatedBy(final EmployeeDto createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public List<ReservationDetailDto> getDetails() {
    return details;
  }

  public void setDetails(List<ReservationDetailDto> details) {
    this.details = details;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ReservationDto)) return false;
    ReservationDto that = (ReservationDto) o;
    return Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "ReservationDto{"
        + "id="
        + id
        + ", rentalId="
        + rentalId
        + ", owner="
        + owner
        + ", createdAt="
        + createdAt
        + ", checkInAt="
        + checkInAt
        + ", checkOutAt="
        + checkOutAt
        + ", depositAmount="
        + depositAmount
        + ", depositPercent="
        + depositPercent
        + ", specialRequirements='"
        + specialRequirements
        + '\''
        + ", paymentMethodCode="
        + paymentMethodCode
        + ", fee="
        + fee
        + ", statusCode="
        + statusCode
        + ", createdBy="
        + createdBy
        + ", details="
        + details
        + ", total="
        + total
        + ", timeElapsedMins="
        + timeElapsedMins
        + ", refund="
        + refund
        + '}';
  }
}