package vn.lotusviet.hotelmgmt.model.dto.stats;

public class ReservationMonthStatsRecord {

  private int year;
  private int month;
  private int numSuccessReservation;
  private int numCancelReservation;

  public ReservationMonthStatsRecord() {}

  public ReservationMonthStatsRecord(
      int year, int month, int numSuccessReservation, int numCancelReservation) {
    this.year = year;
    this.month = month;
    this.numSuccessReservation = numSuccessReservation;
    this.numCancelReservation = numCancelReservation;
  }

  public int getYear() {
    return year;
  }

  public ReservationMonthStatsRecord setYear(int year) {
    this.year = year;
    return this;
  }

  public int getMonth() {
    return month;
  }

  public ReservationMonthStatsRecord setMonth(int month) {
    this.month = month;
    return this;
  }

  public int getNumSuccessReservation() {
    return numSuccessReservation;
  }

  public ReservationMonthStatsRecord setNumSuccessReservation(int numSuccessReservation) {
    this.numSuccessReservation = numSuccessReservation;
    return this;
  }

  public int getNumCancelReservation() {
    return numCancelReservation;
  }

  public ReservationMonthStatsRecord setNumCancelReservation(int numCancelReservation) {
    this.numCancelReservation = numCancelReservation;
    return this;
  }

  @Override
  public String toString() {
    return "ReservationMonthStatsRecord{"
        + "year="
        + year
        + ", month="
        + month
        + ", numSuccessReservation="
        + numSuccessReservation
        + ", numCancelReservation="
        + numCancelReservation
        + '}';
  }
}