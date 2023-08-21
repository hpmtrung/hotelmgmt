package vn.lotusviet.hotelmgmt.exception.entity;

import vn.lotusviet.hotelmgmt.exception.EntityNotFoundException;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;

public class RentalDetailNotFoundException extends EntityNotFoundException {
  private static final long serialVersionUID = 8712327924454974955L;

  public RentalDetailNotFoundException(long id) {
    super(RentalDetail.class.getSimpleName(), String.valueOf(id));
  }
}