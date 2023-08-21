package vn.lotusviet.hotelmgmt.exception;

public class ReservationNotFoundException extends EntityNotFoundException {

  private static final long serialVersionUID = 6678609478806350732L;

  public ReservationNotFoundException(long id) {
    super("reservation", String.valueOf(id));
  }
}