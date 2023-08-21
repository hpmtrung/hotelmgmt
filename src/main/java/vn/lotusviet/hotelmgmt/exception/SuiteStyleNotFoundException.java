package vn.lotusviet.hotelmgmt.exception;

public class SuiteStyleNotFoundException extends EntityNotFoundException {
  private static final long serialVersionUID = -5560044591080813652L;

  public SuiteStyleNotFoundException(int id) {
    super("suiteStyle", String.valueOf(id));
  }
}