package vn.lotusviet.hotelmgmt.exception.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RentalDetailUpdateIllegalException extends RuntimeException {
  private static final long serialVersionUID = -5123308557222441945L;

  public RentalDetailUpdateIllegalException(String message) {
    super(message);
  }

  public RentalDetailUpdateIllegalException(String message, Throwable cause) {
    super(message, cause);
  }
}