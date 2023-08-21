package vn.lotusviet.hotelmgmt.exception;

import vn.lotusviet.hotelmgmt.core.exception.ConstraintViolationException;

public class PromotionInvalidDateException extends ConstraintViolationException {
  private static final long serialVersionUID = 94076104568632050L;

  public PromotionInvalidDateException() {
    super("promotion", "date", "invalid");
  }
}