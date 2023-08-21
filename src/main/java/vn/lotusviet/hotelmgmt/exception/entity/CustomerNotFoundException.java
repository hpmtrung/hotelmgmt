package vn.lotusviet.hotelmgmt.exception.entity;

import vn.lotusviet.hotelmgmt.exception.EntityNotFoundException;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;

public class CustomerNotFoundException extends EntityNotFoundException {
  private static final long serialVersionUID = -802810883875900905L;

  public CustomerNotFoundException(String personaId) {
    super(Customer.class.getSimpleName(), personaId);
  }
}