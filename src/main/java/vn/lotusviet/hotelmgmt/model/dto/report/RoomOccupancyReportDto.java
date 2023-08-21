package vn.lotusviet.hotelmgmt.model.dto.report;

import java.util.List;

public class RoomOccupancyReportDto {

  private List<RoomOccupancyRecord> records;

  public List<RoomOccupancyRecord> getRecords() {
    return records;
  }

  public RoomOccupancyReportDto setRecords(List<RoomOccupancyRecord> roomOccupancyRecords) {
    this.records = roomOccupancyRecords;
    return this;
  }

  @Override
  public String toString() {
    return "RoomOccupancyReportDto{" + "roomOccupancyRecords=" + records + '}';
  }

  public static class RoomOccupancyRecord {
    private String month;
    private String suiteTypeName;
    private String suiteStyleName;
    private int occupiedNum;
    private int total;
    private float percent;

    public int getTotal() {
      return total;
    }

    public RoomOccupancyRecord setTotal(int total) {
      this.total = total;
      return this;
    }

    public String getMonth() {
      return month;
    }

    public RoomOccupancyRecord setMonth(final String month) {
      this.month = month;
      return this;
    }

    public String getSuiteTypeName() {
      return suiteTypeName;
    }

    public RoomOccupancyRecord setSuiteTypeName(String suiteTypeName) {
      this.suiteTypeName = suiteTypeName;
      return this;
    }

    public String getSuiteStyleName() {
      return suiteStyleName;
    }

    public RoomOccupancyRecord setSuiteStyleName(String suiteStyleName) {
      this.suiteStyleName = suiteStyleName;
      return this;
    }

    public int getOccupiedNum() {
      return occupiedNum;
    }

    public RoomOccupancyRecord setOccupiedNum(final int occupiedNum) {
      this.occupiedNum = occupiedNum;
      return this;
    }

    public float getPercent() {
      return percent;
    }

    public RoomOccupancyRecord setPercent(final float percent) {
      this.percent = percent;
      return this;
    }
  }
}