package vn.lotusviet.hotelmgmt.core.exception;

import org.springframework.http.HttpStatus;

public class AuditedLoginNotFoundException extends SimpleMessageException {

  private static final long serialVersionUID = -4788972223880501961L;

  public AuditedLoginNotFoundException() {
    super(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.SERVER_ERROR, "AUDITED_FAILURE");
  }
}