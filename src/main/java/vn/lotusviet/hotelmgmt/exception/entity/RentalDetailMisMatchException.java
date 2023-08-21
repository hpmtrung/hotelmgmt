package vn.lotusviet.hotelmgmt.exception.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RentalDetailMisMatchException extends RuntimeException {
  private static final long serialVersionUID = 8071351910381967562L;

  public RentalDetailMisMatchException() {}
}