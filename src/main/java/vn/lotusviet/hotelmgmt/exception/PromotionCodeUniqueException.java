package vn.lotusviet.hotelmgmt.exception;

import vn.lotusviet.hotelmgmt.core.exception.ConstraintViolationException;

public class PromotionCodeUniqueException extends ConstraintViolationException {
  private static final long serialVersionUID = 5593886273855572207L;

  public PromotionCodeUniqueException() {
    super("promotion", "code", "unique");
  }
}