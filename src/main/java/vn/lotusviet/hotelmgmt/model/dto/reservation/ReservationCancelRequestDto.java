package vn.lotusviet.hotelmgmt.model.dto.reservation;

import javax.validation.constraints.PositiveOrZero;

public class ReservationCancelRequestDto {

  @PositiveOrZero
  private Integer refund;

  public Integer getRefund() {
    return refund;
  }

  public ReservationCancelRequestDto setRefund(Integer refund) {
    this.refund = refund;
    return this;
  }

  @Override
  public String toString() {
    return "ReservationCancelRequestDto{" + "refund=" + refund + '}';
  }
}