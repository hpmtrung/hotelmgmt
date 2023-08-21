package vn.lotusviet.hotelmgmt.model.dto.room;

public class SuiteSearchRequestDto {

  private String styleIds = "";
  private String typeIds = "";
  private Boolean hasPromotion = false;
  private Integer priceFrom;
  private Integer priceTo;
  private Long reservationIdExcluded;
  private Long rentalDetailIdExcluded;

  public Long getReservationIdExcluded() {
    return reservationIdExcluded;
  }

  public SuiteSearchRequestDto setReservationIdExcluded(Long reservationIdExcluded) {
    this.reservationIdExcluded = reservationIdExcluded;
    return this;
  }

  public Long getRentalDetailIdExcluded() {
    return rentalDetailIdExcluded;
  }

  public SuiteSearchRequestDto setRentalDetailIdExcluded(Long rentalDetailIdExcluded) {
    this.rentalDetailIdExcluded = rentalDetailIdExcluded;
    return this;
  }

  public String getStyleIds() {
    return styleIds;
  }

  public SuiteSearchRequestDto setStyleIds(final String styleIds) {
    this.styleIds = styleIds;
    return this;
  }

  public String getTypeIds() {
    return typeIds;
  }

  public SuiteSearchRequestDto setTypeIds(final String typeIds) {
    this.typeIds = typeIds;
    return this;
  }

  public Boolean getHasPromotion() {
    return hasPromotion;
  }

  public SuiteSearchRequestDto setHasPromotion(final Boolean hasPromotion) {
    this.hasPromotion = hasPromotion;
    return this;
  }

  public Integer getPriceFrom() {
    return priceFrom;
  }

  public SuiteSearchRequestDto setPriceFrom(final Integer priceFrom) {
    this.priceFrom = priceFrom;
    return this;
  }

  public Integer getPriceTo() {
    return priceTo;
  }

  public SuiteSearchRequestDto setPriceTo(final Integer priceTo) {
    this.priceTo = priceTo;
    return this;
  }

  @Override
  public String toString() {
    return "SuiteSearchRequestDto{"
        + "styleIds='"
        + styleIds
        + '\''
        + ", typeIds='"
        + typeIds
        + '\''
        + ", hasPromotion="
        + hasPromotion
        + ", priceFrom="
        + priceFrom
        + ", priceTo="
        + priceTo
        + ", reservationIdExcluded="
        + reservationIdExcluded
        + ", rentalDetailIdExcluded="
        + rentalDetailIdExcluded
        + '}';
  }
}