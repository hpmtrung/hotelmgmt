package vn.lotusviet.hotelmgmt.core.exception;

import org.springframework.http.HttpStatus;
import vn.lotusviet.hotelmgmt.core.exception.payload.MessageWithDetailPayload;

import java.util.List;

public class MessageWithDetailException extends SimpleMessageException
    implements MessageWithDetailPayload {

  private static final long serialVersionUID = 1215309690551870958L;
  private final List<ErrorDetail> errorDetails;

  public MessageWithDetailException(
      HttpStatus status,
      ErrorType type,
      String name,
      String message,
      List<ErrorDetail> errorDetails,
      Throwable cause) {
    super(status, type, name, message, cause);
    this.errorDetails = errorDetails;
  }

  public MessageWithDetailException(
      HttpStatus status, ErrorType type, String name, List<ErrorDetail> errorDetails) {
    super(status, type, name);
    this.errorDetails = errorDetails;
  }

  public MessageWithDetailException(
      HttpStatus status,
      ErrorType type,
      String name,
      String message,
      List<ErrorDetail> errorDetails) {
    super(status, type, name, message);
    this.errorDetails = errorDetails;
  }

  @Override
  public List<ErrorDetail> getDetails() {
    return this.errorDetails;
  }
}