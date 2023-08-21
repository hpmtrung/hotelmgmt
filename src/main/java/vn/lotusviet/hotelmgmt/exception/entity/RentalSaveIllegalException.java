package vn.lotusviet.hotelmgmt.exception.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RentalSaveIllegalException extends RuntimeException {

  private static final long serialVersionUID = -8044668563152875599L;

  public RentalSaveIllegalException() {
  }

  public RentalSaveIllegalException(String message, Throwable cause) {
    super(message, cause);
  }

  public RentalSaveIllegalException(String message) {
    super(message);
  }
}