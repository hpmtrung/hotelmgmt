package vn.lotusviet.hotelmgmt.model.dto.rental;

import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;

import javax.validation.constraints.NotNull;

public class RentalPayingRequestDto {

  @NotNull private PaymentMethodCode paymentMethodCode;

  public PaymentMethodCode getPaymentMethodCode() {
    return paymentMethodCode;
  }

  public void setPaymentMethodCode(PaymentMethodCode paymentMethodCode) {
    this.paymentMethodCode = paymentMethodCode;
  }

  @Override
  public String toString() {
    return "RentalPayingRequestDto{" + "paymentMethodCode=" + paymentMethodCode + '}';
  }
}