package vn.lotusviet.hotelmgmt.core.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception wrapper for unknown error.
 */
public class ErrorHandlerNotFoundException extends SimpleMessageException {

  private static final long serialVersionUID = -6740945613430583805L;

  public ErrorHandlerNotFoundException(Throwable cause) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.UNHANDLED_ERROR, "Unknown error handler", cause.getMessage(), cause);
  }

}