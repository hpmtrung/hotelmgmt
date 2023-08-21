package vn.lotusviet.hotelmgmt.model.dto.rental;

import java.time.LocalDate;

public class RentalFilterOption {
  private String personalId;
  private String statuses;
  private LocalDate startDate;
  private LocalDate endDate;

  public RentalFilterOption() {}

  public RentalFilterOption(
      String personalId, String statuses, LocalDate startDate, LocalDate endDate) {
    this.personalId = personalId;
    this.statuses = statuses;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public String getPersonalId() {
    return personalId;
  }

  public RentalFilterOption setPersonalId(String personalId) {
    this.personalId = personalId;
    return this;
  }

  public String getStatuses() {
    return statuses;
  }

  public RentalFilterOption setStatuses(String statuses) {
    this.statuses = statuses;
    return this;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public RentalFilterOption setStartDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public RentalFilterOption setEndDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  @Override
  public String toString() {
    return "RentalFilterOption{"
        + "personalId='"
        + personalId
        + '\''
        + ", rentalStatusCodes="
        + statuses
        + ", startDate="
        + startDate
        + ", endDate="
        + endDate
        + '}';
  }
}