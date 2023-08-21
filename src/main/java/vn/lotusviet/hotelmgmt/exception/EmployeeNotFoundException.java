package vn.lotusviet.hotelmgmt.exception;

public class EmployeeNotFoundException extends EntityNotFoundException {

  private static final long serialVersionUID = -1292201137078889863L;

  public EmployeeNotFoundException(int id) {
    super("employee", String.valueOf(id));
  }
}