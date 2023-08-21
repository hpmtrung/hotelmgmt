package vn.lotusviet.hotelmgmt.model.dto.reservation;

import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Map;

public class ReservationCreateDto {

  @NotNull private LocalDate checkInAt;
  @NotNull private LocalDate checkOutAt;

  @NotNull
  @Size(min = 1)
  private Map<Integer, Integer> suiteIdMapping;

  @NotNull private CustomerDto customer;
  private String specialRequirements;
  @NotNull private PaymentMethodCode paymentMethodCode;

  @Positive
  @NotNull
  private Integer depositAmount;

  public LocalDate getCheckInAt() {
    return checkInAt;
  }

  public ReservationCreateDto setCheckInAt(final LocalDate checkInAt) {
    this.checkInAt = checkInAt;
    return this;
  }

  public LocalDate getCheckOutAt() {
    return checkOutAt;
  }

  public ReservationCreateDto setCheckOutAt(final LocalDate checkOutAt) {
    this.checkOutAt = checkOutAt;
    return this;
  }

  public Map<Integer, Integer> getSuiteIdMapping() {
    return suiteIdMapping;
  }

  public ReservationCreateDto setSuiteIdMapping(final Map<Integer, Integer> suiteIdMapping) {
    this.suiteIdMapping = suiteIdMapping;
    return this;
  }

  public CustomerDto getCustomer() {
    return customer;
  }

  public ReservationCreateDto setCustomer(final CustomerDto customer) {
    this.customer = customer;
    return this;
  }

  public String getSpecialRequirements() {
    return specialRequirements;
  }

  public ReservationCreateDto setSpecialRequirements(final String specialRequirements) {
    this.specialRequirements = specialRequirements;
    return this;
  }

  public PaymentMethodCode getPaymentMethodCode() {
    return paymentMethodCode;
  }

  public ReservationCreateDto setPaymentMethodCode(final PaymentMethodCode paymentMethodCode) {
    this.paymentMethodCode = paymentMethodCode;
    return this;
  }

  public Integer getDepositAmount() {
    return depositAmount;
  }

  public ReservationCreateDto setDepositAmount(final Integer depositAmount) {
    this.depositAmount = depositAmount;
    return this;
  }

  @Override
  public String toString() {
    return "ReservationCreateDto{"
        + "checkInAt="
        + checkInAt
        + ", checkOutAt="
        + checkOutAt
        + ", suiteIdMapping="
        + suiteIdMapping
        + ", customer="
        + customer
        + ", specialRequirements='"
        + specialRequirements
        + '\''
        + ", paymentMethodCode="
        + paymentMethodCode
        + ", depositAmount="
        + depositAmount
        + '}';
  }
}