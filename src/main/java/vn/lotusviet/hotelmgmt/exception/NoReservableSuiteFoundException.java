package vn.lotusviet.hotelmgmt.exception;

public class NoReservableSuiteFoundException extends RuntimeException {

  private static final long serialVersionUID = 5816685772308868366L;

  public NoReservableSuiteFoundException(String message) {
    super(message);
  }
}