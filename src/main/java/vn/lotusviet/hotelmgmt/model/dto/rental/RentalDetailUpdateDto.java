package vn.lotusviet.hotelmgmt.model.dto.rental;

import java.time.LocalDateTime;

public class RentalDetailUpdateDto {

  private LocalDateTime checkOutAt;

  private Integer discountAmount;

  private Integer extraAmount;

  public LocalDateTime getCheckOutAt() {
    return checkOutAt;
  }

  public void setCheckOutAt(LocalDateTime checkOutAt) {
    this.checkOutAt = checkOutAt;
  }

  public Integer getDiscountAmount() {
    return discountAmount;
  }

  public void setDiscountAmount(Integer discountAmount) {
    this.discountAmount = discountAmount;
  }

  public Integer getExtraAmount() {
    return extraAmount;
  }

  public void setExtraAmount(Integer extraAmount) {
    this.extraAmount = extraAmount;
  }

  @Override
  public String toString() {
    return "RentalDetailUpdateDto{"
        + "checkOutAt="
        + checkOutAt
        + ", discountAmount="
        + discountAmount
        + ", extraAmount="
        + extraAmount
        + '}';
  }
}