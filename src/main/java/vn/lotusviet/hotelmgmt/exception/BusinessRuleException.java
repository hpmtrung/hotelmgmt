package vn.lotusviet.hotelmgmt.exception;

public class BusinessRuleException extends RuntimeException {

  private static final long serialVersionUID = -2509047795288989008L;

  public BusinessRuleException(String message) {
    super(message);
  }

  public BusinessRuleException(String message, Throwable cause) {
    super(message, cause);
  }
}