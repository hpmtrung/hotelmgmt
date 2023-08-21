package vn.lotusviet.hotelmgmt.exception.entity;

import vn.lotusviet.hotelmgmt.exception.EntityNotFoundException;

public class InvoiceNotFoundException extends EntityNotFoundException {
  private static final long serialVersionUID = -554538232654959188L;

  public InvoiceNotFoundException(long id) {
    super("invoice", String.valueOf(id));
  }
}