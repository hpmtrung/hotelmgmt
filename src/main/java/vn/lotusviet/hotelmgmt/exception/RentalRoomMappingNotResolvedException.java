package vn.lotusviet.hotelmgmt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RentalRoomMappingNotResolvedException extends RuntimeException {

  private static final long serialVersionUID = 2471851542631632340L;

  public RentalRoomMappingNotResolvedException() {}

  public RentalRoomMappingNotResolvedException(String message) {
    super(message);
  }
}