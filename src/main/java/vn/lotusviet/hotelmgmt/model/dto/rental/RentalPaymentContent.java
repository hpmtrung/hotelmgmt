package vn.lotusviet.hotelmgmt.model.dto.rental;

public enum RentalPaymentContent {
  PAY_DEPOSIT("tiền đặt cọc"),
  PAY_RENTAL_DISCOUNT("tiền giảm cho khách đoàn");

  private final String value;

  RentalPaymentContent(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}