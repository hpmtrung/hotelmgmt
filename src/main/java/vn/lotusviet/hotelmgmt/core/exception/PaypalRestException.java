package vn.lotusviet.hotelmgmt.core.exception;

import org.springframework.http.HttpStatus;

public class PaypalRestException extends SimpleMessageException {

  private static final long serialVersionUID = -4555839354951095001L;

  public PaypalRestException(String message, Throwable cause) {
    super(
        HttpStatus.INTERNAL_SERVER_ERROR,
        ErrorType.SERVER_ERROR,
        "PAYPAL_ERROR",
        message != null ? message : cause.getMessage(),
        cause);
  }

  public PaypalRestException(Throwable cause) {
    this(null, cause);
  }

  public PaypalRestException(String message) {
    this(message, null);
  }
}