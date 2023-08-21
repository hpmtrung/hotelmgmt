package vn.lotusviet.hotelmgmt.exception.entity;

import vn.lotusviet.hotelmgmt.exception.EntityNotFoundException;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatusCode;

public class ReservationStatusNotFoundException extends EntityNotFoundException {

  private static final long serialVersionUID = -43878482732644257L;

  public ReservationStatusNotFoundException(ReservationStatusCode code) {
    super("reservationStatus", code.name());
  }
}