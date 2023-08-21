package vn.lotusviet.hotelmgmt.exception;

import vn.lotusviet.hotelmgmt.core.exception.ConstraintViolationException;

public class RoomStatusUpdateIllegalStateException extends ConstraintViolationException {

  private static final long serialVersionUID = -7048558212597383576L;

  public RoomStatusUpdateIllegalStateException() {
    super("room", "status", "illegalUpdate");
  }
}