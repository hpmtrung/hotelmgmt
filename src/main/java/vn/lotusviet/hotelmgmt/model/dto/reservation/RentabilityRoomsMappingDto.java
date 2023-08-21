package vn.lotusviet.hotelmgmt.model.dto.reservation;

import vn.lotusviet.hotelmgmt.model.dto.room.RoomDto;

import java.util.List;
import java.util.Map;

public class RentabilityRoomsMappingDto {

  private Map<Integer, List<RoomDto>> roomMapping;

  public Map<Integer, List<RoomDto>> getRoomMapping() {
    return roomMapping;
  }

  public RentabilityRoomsMappingDto setRoomMapping(final Map<Integer, List<RoomDto>> roomMapping) {
    this.roomMapping = roomMapping;
    return this;
  }

  @Override
  public String toString() {
    return "RentabilityRoomsMappingDto{" + "roomMapping=" + roomMapping + '}';
  }
}