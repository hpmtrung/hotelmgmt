package vn.lotusviet.hotelmgmt.core.paying.paypal;

public class PaymentSource {
  private Paypal paypal;

  public Paypal getPaypal() {
    return paypal;
  }

  public PaymentSource setPaypal(Paypal paypal) {
    this.paypal = paypal;
    return this;
  }
}