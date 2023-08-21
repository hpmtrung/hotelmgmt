package vn.lotusviet.hotelmgmt.exception.entity;

import vn.lotusviet.hotelmgmt.exception.EntityNotFoundException;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;

public class RentalNotFoundException extends EntityNotFoundException {
  private static final long serialVersionUID = 4994924163830364559L;

  public RentalNotFoundException(long id) {
    super(Rental.class.getSimpleName(), String.valueOf(id));
  }
}