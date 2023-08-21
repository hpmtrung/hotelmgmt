package vn.lotusviet.hotelmgmt.exception;

public class ServiceNotFoundException extends EntityNotFoundException {

  private static final long serialVersionUID = 632878130974709835L;

  public ServiceNotFoundException(int id) {
    super("service", String.valueOf(id));
  }
}