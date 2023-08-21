package vn.lotusviet.hotelmgmt.exception.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReservationSaveIllegalStateException extends RuntimeException {
  private static final long serialVersionUID = 7235860560956314465L;

  public ReservationSaveIllegalStateException(String message) {
    super(message);
  }

  public ReservationSaveIllegalStateException(String message, Throwable cause) {
    super(message, cause);
  }
}