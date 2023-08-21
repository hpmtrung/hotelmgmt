package vn.lotusviet.hotelmgmt.model.dto.system;

import vn.lotusviet.hotelmgmt.model.dto.stats.DepartmentEmployeeStatsRecord;
import vn.lotusviet.hotelmgmt.model.dto.stats.ReservationMonthStatsRecord;
import vn.lotusviet.hotelmgmt.model.dto.stats.RoomStatusStatsRecord;

import java.util.List;

public class AdminDashboardDataDto {

  private long totalCustomer;
  private short totalSuite;
  private short totalRoom;
  private short totalEmployee;
  private List<DepartmentEmployeeStatsRecord> departmentEmployeeStatsRecords;
  private List<RoomStatusStatsRecord> roomStatusStatsRecords;
  private List<ReservationMonthStatsRecord> reservationMonthStatsRecords;

  public short getTotalEmployee() {
    return totalEmployee;
  }

  public AdminDashboardDataDto setTotalEmployee(short totalEmployee) {
    this.totalEmployee = totalEmployee;
    return this;
  }

  public long getTotalCustomer() {
    return totalCustomer;
  }

  public AdminDashboardDataDto setTotalCustomer(long totalCustomer) {
    this.totalCustomer = totalCustomer;
    return this;
  }

  public int getTotalSuite() {
    return totalSuite;
  }

  public AdminDashboardDataDto setTotalSuite(short totalSuite) {
    this.totalSuite = totalSuite;
    return this;
  }

  public int getTotalRoom() {
    return totalRoom;
  }

  public AdminDashboardDataDto setTotalRoom(short totalRoom) {
    this.totalRoom = totalRoom;
    return this;
  }

  public List<DepartmentEmployeeStatsRecord> getEmployeeDepartmentStatsRecords() {
    return departmentEmployeeStatsRecords;
  }

  public AdminDashboardDataDto setDepartmentEmployeeStatsRecords(
      List<DepartmentEmployeeStatsRecord> departmentEmployeeStatsRecords) {
    this.departmentEmployeeStatsRecords = departmentEmployeeStatsRecords;
    return this;
  }

  public List<RoomStatusStatsRecord> getRoomStatusStatsRecords() {
    return roomStatusStatsRecords;
  }

  public AdminDashboardDataDto setRoomStatusStatsRecords(
      List<RoomStatusStatsRecord> roomStatusStatsRecords) {
    this.roomStatusStatsRecords = roomStatusStatsRecords;
    return this;
  }

  public List<ReservationMonthStatsRecord> getReservationMonthStatsRecords() {
    return reservationMonthStatsRecords;
  }

  public AdminDashboardDataDto setReservationMonthStatsRecords(
      List<ReservationMonthStatsRecord> reservationMonthStatsRecords) {
    this.reservationMonthStatsRecords = reservationMonthStatsRecords;
    return this;
  }

  @Override
  public String toString() {
    return "AdminDashboardDataDto{"
        + "totalCustomer="
        + totalCustomer
        + ", totalSuite="
        + totalSuite
        + ", totalRoom="
        + totalRoom
        + ", departmentEmployeeStatsRecords="
        + departmentEmployeeStatsRecords
        + ", roomStatusStatsRecords="
        + roomStatusStatsRecords
        + ", reservationMonthStatsRecords="
        + reservationMonthStatsRecords
        + '}';
  }
}