package vn.lotusviet.hotelmgmt.exception.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReservationUpdateIllegalException extends RuntimeException {

  private static final long serialVersionUID = 1067896441908776811L;

  public ReservationUpdateIllegalException(String message) {
    super(message);
  }

  public ReservationUpdateIllegalException(String message, Throwable cause) {
    super(message, cause);
  }
}