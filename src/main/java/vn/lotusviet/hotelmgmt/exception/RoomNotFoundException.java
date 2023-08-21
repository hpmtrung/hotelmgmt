package vn.lotusviet.hotelmgmt.exception;

public class RoomNotFoundException extends EntityNotFoundException {

  private static final long serialVersionUID = 8411641765662514072L;

  public RoomNotFoundException(int id) {
    super("room", String.valueOf(id));
  }

}