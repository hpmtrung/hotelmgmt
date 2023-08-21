package vn.lotusviet.hotelmgmt.core.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends SimpleMessageException {

  public static final String DEFAULT_NAME = "INVALID_REQUEST";
  public static final String DEFAULT_MESSAGE =
      "Request is not well-formed, syntactically incorrect, or violates schema.";

  private static final long serialVersionUID = -8824796380264570254L;

  public BadRequestException(String message) {
    super(HttpStatus.BAD_REQUEST, ErrorType.BAD_REQUEST_ERROR, DEFAULT_NAME, message);
  }

  public BadRequestException(String name, Throwable cause) {
    super(
        HttpStatus.BAD_REQUEST,
        ErrorType.BAD_REQUEST_ERROR,
        name,
        cause.getMessage() != null ? cause.getMessage() : DEFAULT_MESSAGE,
        cause);
  }

  public BadRequestException(Throwable cause) {
    super(
        HttpStatus.BAD_REQUEST,
        ErrorType.BAD_REQUEST_ERROR,
        DEFAULT_NAME,
        cause.getMessage() != null ? cause.getMessage() : DEFAULT_MESSAGE,
        cause);
  }
}