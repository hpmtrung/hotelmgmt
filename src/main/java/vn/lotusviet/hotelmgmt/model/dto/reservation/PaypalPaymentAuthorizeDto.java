package vn.lotusviet.hotelmgmt.model.dto.reservation;

import javax.validation.constraints.NotBlank;

public class PaypalPaymentAuthorizeDto {

  @NotBlank private String token;
  @NotBlank private String payerId;

  public String getToken() {
    return token;
  }

  public PaypalPaymentAuthorizeDto setToken(final String token) {
    this.token = token;
    return this;
  }

  public String getPayerId() {
    return payerId;
  }

  public PaypalPaymentAuthorizeDto setPayerId(final String payerId) {
    this.payerId = payerId;
    return this;
  }

  @Override
  public String toString() {
    return "PaypalPaymentAuthorizeDto{"
        + "token='"
        + token
        + '\''
        + ", payerId='"
        + payerId
        + '\''
        + '}';
  }
}