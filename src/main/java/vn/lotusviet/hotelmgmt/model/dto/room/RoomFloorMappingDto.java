package vn.lotusviet.hotelmgmt.model.dto.room;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomFloorMappingDto {

  private final List<RoomDto> rooms;

  public RoomFloorMappingDto(List<RoomDto> rooms) {
    this.rooms = rooms;
  }

  public List<RoomDto> getRooms() {
    return rooms;
  }

  @JsonProperty("floorMapping")
  public Map<Integer, List<Integer>> getFloorMapping() {
    Map<Integer, List<Integer>> map = new HashMap<>();
    for (int i = 0; i < rooms.size(); i++) {
      final RoomDto room = rooms.get(i);
      int floor = room.getFloor();
      map.computeIfAbsent(floor, (key) -> new ArrayList<>());
      map.get(floor).add(i);
    }
    return map;
  }
}