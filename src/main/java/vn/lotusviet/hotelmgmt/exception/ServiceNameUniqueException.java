package vn.lotusviet.hotelmgmt.exception;

import vn.lotusviet.hotelmgmt.core.exception.ConstraintViolationException;

public class ServiceNameUniqueException extends ConstraintViolationException {

  private static final long serialVersionUID = -5570725997901218871L;

  public ServiceNameUniqueException() {
    super("service", "name", "unique");
  }
}