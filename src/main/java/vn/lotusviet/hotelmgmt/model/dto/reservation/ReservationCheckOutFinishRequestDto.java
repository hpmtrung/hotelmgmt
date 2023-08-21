package vn.lotusviet.hotelmgmt.model.dto.reservation;

import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class ReservationCheckOutFinishRequestDto {

  @NotNull private Long reservationId;
  @NotNull private CustomerDto customer;
  private String specialRequirements;
  @NotNull private PaymentMethodCode paymentMethodCode;

  @Positive
  @NotNull
  private Integer depositAmount;

  public Integer getDepositAmount() {
    return depositAmount;
  }

  public void setDepositAmount(Integer depositAmount) {
    this.depositAmount = depositAmount;
  }

  public @NotNull PaymentMethodCode getPaymentMethodCode() {
    return paymentMethodCode;
  }

  public ReservationCheckOutFinishRequestDto setPaymentMethodCode(
      final @NotNull PaymentMethodCode paymentMethodCode) {
    this.paymentMethodCode = paymentMethodCode;
    return this;
  }

  public Long getReservationId() {
    return reservationId;
  }

  public ReservationCheckOutFinishRequestDto setReservationId(final Long reservationId) {
    this.reservationId = reservationId;
    return this;
  }

  public CustomerDto getCustomer() {
    return customer;
  }

  public ReservationCheckOutFinishRequestDto setCustomer(final CustomerDto customer) {
    this.customer = customer;
    return this;
  }

  public String getSpecialRequirements() {
    return specialRequirements;
  }

  public ReservationCheckOutFinishRequestDto setSpecialRequirements(
      final String specialRequirements) {
    this.specialRequirements = specialRequirements;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ReservationCheckOutFinishRequestDto)) return false;
    ReservationCheckOutFinishRequestDto that = (ReservationCheckOutFinishRequestDto) o;
    return Objects.equals(getReservationId(), that.getReservationId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getReservationId());
  }

  @Override
  public String toString() {
    return "ReservationCheckOutFinishRequestDto{"
        + "reservationId="
        + reservationId
        + ", customer="
        + customer
        + ", specialRequirements='"
        + specialRequirements
        + '\''
        + '}';
  }
}