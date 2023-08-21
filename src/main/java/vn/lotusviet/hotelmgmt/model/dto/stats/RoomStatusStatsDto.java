package vn.lotusviet.hotelmgmt.model.dto.stats;

import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatusCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomStatusStatsDto {

  private List<Record> records = new ArrayList<>();

  public List<Record> getRecords() {
    return records;
  }

  public RoomStatusStatsDto setRecords(final List<Record> records) {
    this.records = records;
    return this;
  }

  public Integer getTotal() {
    return records.stream().mapToInt(Record::getRoomNum).sum();
  }

  @Override
  public String toString() {
    return "RoomStatusStatsDto{" + "records=" + records + '}';
  }

  public static class Record implements Serializable {

    private static final long serialVersionUID = -2128332814555615355L;

    private RoomStatusCode statusCode;
    private int roomNum;

    public RoomStatusCode getStatusCode() {
      return statusCode;
    }

    public Record setStatusCode(final RoomStatusCode statusCode) {
      this.statusCode = statusCode;
      return this;
    }

    public int getRoomNum() {
      return roomNum;
    }

    public Record setRoomNum(final int roomNum) {
      this.roomNum = roomNum;
      return this;
    }

    @Override
    public String toString() {
      return "RoomOccupancyRecord{" + "statusCode=" + statusCode + ", roomNum=" + roomNum + '}';
    }
  }
}