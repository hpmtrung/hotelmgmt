package vn.lotusviet.hotelmgmt.exception.entity;

import vn.lotusviet.hotelmgmt.exception.EntityNotFoundException;

public class DepartmentNotFoundException extends EntityNotFoundException {
  private static final long serialVersionUID = 3599743291238166125L;

  public DepartmentNotFoundException(int id) {
    super("department", String.valueOf(id));
  }
}