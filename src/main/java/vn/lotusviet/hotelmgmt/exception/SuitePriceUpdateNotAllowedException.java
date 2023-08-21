package vn.lotusviet.hotelmgmt.exception;

public class SuitePriceUpdateNotAllowedException extends RuntimeException {
  private static final long serialVersionUID = 4195862970015541194L;

  public SuitePriceUpdateNotAllowedException(String message) {
    super(message);
  }
}