package vn.lotusviet.hotelmgmt.exception.entity;

import vn.lotusviet.hotelmgmt.exception.EntityNotFoundException;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatus;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatusCode;

public class RoomStatusCodeNotFoundException extends EntityNotFoundException {
  private static final long serialVersionUID = -2249701311280394201L;

  public RoomStatusCodeNotFoundException(RoomStatusCode code) {
    super(RoomStatus.class.getSimpleName(), code.name());
  }
}