package vn.lotusviet.hotelmgmt.exception;

import vn.lotusviet.hotelmgmt.core.exception.ConstraintViolationException;

public class EmailDuplicateException extends ConstraintViolationException {

  private static final long serialVersionUID = 1L;

  public EmailDuplicateException() {
    super("account", "email", "notUnique");
  }
}