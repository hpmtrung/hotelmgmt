package vn.lotusviet.hotelmgmt.core.exception;

import org.springframework.http.HttpStatus;
import vn.lotusviet.hotelmgmt.core.exception.payload.SimpleMessagePayload;

public class SimpleMessageException extends RuntimeException implements SimpleMessagePayload {

  private static final long serialVersionUID = -5105425769581919701L;

  private final HttpStatus status;
  private final ErrorType type;
  private final String name;

  public SimpleMessageException(
      HttpStatus status, ErrorType type, String name, String message, Throwable cause) {
    super(message, cause);
    this.status = status;
    this.type = type;
    this.name = name;
  }

  public SimpleMessageException(HttpStatus status, ErrorType type, String name) {
    this.status = status;
    this.type = type;
    this.name = name;
  }

  public SimpleMessageException(HttpStatus status, ErrorType type, String name, String message) {
    super(message);
    this.status = status;
    this.type = type;
    this.name = name;
  }

  @Override
  public HttpStatus getStatus() {
    return this.status;
  }

  @Override
  public ErrorType getErrorType() {
    return this.type;
  }

  @Override
  public String getName() {
    return this.name;
  }
}