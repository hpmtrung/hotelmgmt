package vn.lotusviet.hotelmgmt.exception;

import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;

public class PaymentMethodNotFoundException extends EntityNotFoundException {

  private static final long serialVersionUID = 5823851282746332273L;

  public PaymentMethodNotFoundException(PaymentMethodCode code) {
    super("paymentMethod", code.name());
  }
}