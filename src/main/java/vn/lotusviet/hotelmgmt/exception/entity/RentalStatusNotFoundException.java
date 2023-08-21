package vn.lotusviet.hotelmgmt.exception.entity;

import vn.lotusviet.hotelmgmt.exception.EntityNotFoundException;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatusCode;

public class RentalStatusNotFoundException extends EntityNotFoundException {
  public RentalStatusNotFoundException(RentalStatusCode code) {
    super("rentalStatus", "code");
  }
}