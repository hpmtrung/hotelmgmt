package vn.lotusviet.hotelmgmt.model.dto.room;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SuiteRoomAddDto {

  @NotNull private List<RoomUpsertDto> rooms;

  public List<RoomUpsertDto> getRooms() {
    return rooms;
  }

  public void setRooms(List<RoomUpsertDto> rooms) {
    this.rooms = rooms;
  }

  @Override
  public String toString() {
    return "SuiteRoomAddDto{" + "rooms=" + rooms + '}';
  }
}