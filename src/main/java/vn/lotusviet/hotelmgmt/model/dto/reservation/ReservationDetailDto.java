package vn.lotusviet.hotelmgmt.model.dto.reservation;

import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;

import java.util.List;

public class ReservationDetailDto {

  private SuiteDto suite;
  private int roomNum;
  private int originalSubTotal;
  private int totalDiscountFromPromotion;
  private int subTotal;
  private int suitePrice;
  private int vat;
  private List<PromotionDto> promotions;

  public int getVat() {
    return vat;
  }

  public ReservationDetailDto setVat(int vat) {
    this.vat = vat;
    return this;
  }

  public int getSuitePrice() {
    return suitePrice;
  }

  public ReservationDetailDto setSuitePrice(int suitePrice) {
    this.suitePrice = suitePrice;
    return this;
  }

  public int getOriginalSubTotal() {
    return originalSubTotal;
  }

  public void setOriginalSubTotal(int originalSubTotal) {
    this.originalSubTotal = originalSubTotal;
  }

  public int getTotalDiscountFromPromotion() {
    return totalDiscountFromPromotion;
  }

  public void setTotalDiscountFromPromotion(int totalDiscountFromPromotion) {
    this.totalDiscountFromPromotion = totalDiscountFromPromotion;
  }

  public int getSubTotal() {
    return subTotal;
  }

  public void setSubTotal(int subTotal) {
    this.subTotal = subTotal;
  }

  public List<PromotionDto> getPromotions() {
    return promotions;
  }

  public ReservationDetailDto setPromotions(final List<PromotionDto> promotions) {
    this.promotions = promotions;
    return this;
  }

  public SuiteDto getSuite() {
    return suite;
  }

  public ReservationDetailDto setSuite(final SuiteDto suite) {
    this.suite = suite;
    return this;
  }

  public int getRoomNum() {
    return roomNum;
  }

  public ReservationDetailDto setRoomNum(final int roomNum) {
    this.roomNum = roomNum;
    return this;
  }

  @Override
  public String toString() {
    return "ReservationDetailDto{"
        + "suite="
        + suite
        + ", roomNum="
        + roomNum
        + ", originalSubTotal="
        + originalSubTotal
        + ", totalDiscountFromPromotion="
        + totalDiscountFromPromotion
        + ", subTotal="
        + subTotal
        + ", suitePrice="
        + suitePrice
        + ", vat="
        + vat
        + ", promotions="
        + promotions
        + '}';
  }
}