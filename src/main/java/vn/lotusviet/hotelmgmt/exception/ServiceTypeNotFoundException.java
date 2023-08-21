package vn.lotusviet.hotelmgmt.exception;

public class ServiceTypeNotFoundException extends EntityNotFoundException {

  private static final long serialVersionUID = -3524687973372525240L;

  public ServiceTypeNotFoundException(int id) {
    super("service", String.valueOf(id));
  }
}