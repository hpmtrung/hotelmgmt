package vn.lotusviet.hotelmgmt.exception.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RentalDetailPayingIllegalException extends RuntimeException {
  private static final long serialVersionUID = -4181568809745523340L;

  public RentalDetailPayingIllegalException() {}

  public RentalDetailPayingIllegalException(String message) {
    super(message);
  }

  public RentalDetailPayingIllegalException(String message, Throwable cause) {
    super(message, cause);
  }
}