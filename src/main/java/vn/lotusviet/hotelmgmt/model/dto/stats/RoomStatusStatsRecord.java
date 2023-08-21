package vn.lotusviet.hotelmgmt.model.dto.stats;

import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatusCode;

import java.util.Objects;

public class RoomStatusStatsRecord {

  private RoomStatusCode statusCode;
  private int numRoom;

  public RoomStatusStatsRecord() {}

  public RoomStatusStatsRecord(RoomStatusCode statusCode, int numRoom) {
    this.statusCode = Objects.requireNonNull(statusCode);
    this.numRoom = numRoom;
  }

  public RoomStatusCode getStatusCode() {
    return statusCode;
  }

  public int getNumRoom() {
    return numRoom;
  }

  @Override
  public String toString() {
    return "RoomStatusStatsRecord{" + "statusCode=" + statusCode + ", numRoom=" + numRoom + '}';
  }
}