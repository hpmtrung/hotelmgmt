package vn.lotusviet.hotelmgmt.exception.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReservationCancelNotAllowException extends RuntimeException {

  private static final long serialVersionUID = -1068196940391557426L;

  public ReservationCancelNotAllowException(String message) {
    super(message);
  }

  public ReservationCancelNotAllowException(String message, Throwable cause) {
    super(message, cause);
  }
}