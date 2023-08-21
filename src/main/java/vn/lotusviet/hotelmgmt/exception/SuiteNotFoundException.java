package vn.lotusviet.hotelmgmt.exception;

public class SuiteNotFoundException extends EntityNotFoundException {

  public SuiteNotFoundException(int id) {
    super("suite", String.valueOf(id));
  }

}