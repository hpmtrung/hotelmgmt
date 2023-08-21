package vn.lotusviet.hotelmgmt.exception;

public class SuiteTypeNotFoundException extends EntityNotFoundException {
  private static final long serialVersionUID = -546969499723668860L;

  public SuiteTypeNotFoundException(int id) {
    super("suiteType", String.valueOf(id));
  }
}