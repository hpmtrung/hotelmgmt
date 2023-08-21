package vn.lotusviet.hotelmgmt.model.dto.reservation;

import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class ReservationUpdateDto {

  @NotNull private LocalDate checkInAt;
  @NotNull private LocalDate checkOutAt;
  private CustomerDto customer;
  private Map<Integer, Integer> suiteIdMapping;
  private Integer timeElapsedMins;
  private String specialRequirements;

  public String getSpecialRequirements() {
    return specialRequirements;
  }

  public ReservationUpdateDto setSpecialRequirements(String specialRequirements) {
    this.specialRequirements = specialRequirements;
    return this;
  }

  public Integer getTimeElapsedMins() {
    return timeElapsedMins;
  }

  public ReservationUpdateDto setTimeElapsedMins(Integer timeElapsedMins) {
    this.timeElapsedMins = timeElapsedMins;
    return this;
  }

  public LocalDate getCheckInAt() {
    return checkInAt;
  }

  public void setCheckInAt(LocalDate checkInAt) {
    this.checkInAt = checkInAt;
  }

  public LocalDate getCheckOutAt() {
    return checkOutAt;
  }

  public void setCheckOutAt(LocalDate checkOutAt) {
    this.checkOutAt = checkOutAt;
  }

  public CustomerDto getCustomer() {
    return customer;
  }

  public ReservationUpdateDto setCustomer(CustomerDto customer) {
    this.customer = customer;
    return this;
  }

  public Map<Integer, Integer> getSuiteIdMapping() {
    return suiteIdMapping;
  }

  public ReservationUpdateDto setSuiteIdMapping(Map<Integer, Integer> suiteIdMapping) {
    this.suiteIdMapping = suiteIdMapping;
    return this;
  }

  @Override
  public String toString() {
    return "ReservationUpdateDto{"
        + "checkInAt="
        + checkInAt
        + ", checkOutAt="
        + checkOutAt
        + ", owner="
        + customer
        + ", details="
        + suiteIdMapping
        + '}';
  }
}