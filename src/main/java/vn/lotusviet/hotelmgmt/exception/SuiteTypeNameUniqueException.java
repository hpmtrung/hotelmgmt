package vn.lotusviet.hotelmgmt.exception;

import vn.lotusviet.hotelmgmt.core.exception.ConstraintViolationException;

public class SuiteTypeNameUniqueException extends ConstraintViolationException {
  private static final long serialVersionUID = 2051133578781773625L;

  public SuiteTypeNameUniqueException() {
    super("suiteType", "name", "unique");
  }
}