package vn.lotusviet.hotelmgmt.core.exception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends SimpleMessageException{

  private static final long serialVersionUID = -8871359668137832701L;

  public InternalServerErrorException(String message, Throwable cause) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.SERVER_ERROR, "INTERNAL_SERVER_ERROR", message, cause);
  }

  public InternalServerErrorException(String message) {
    this(message, null);
  }
}