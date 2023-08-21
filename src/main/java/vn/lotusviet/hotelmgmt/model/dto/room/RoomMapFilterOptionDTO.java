package vn.lotusviet.hotelmgmt.model.dto.room;

public class RoomMapFilterOptionDTO {

  private String typeIds;
  private String styleIds;
  private Boolean hasPromotion;
  private Integer priceFrom;
  private Integer priceTo;
  // if null, then all
  private String statuses;

  public String getStatuses() {
    return statuses;
  }

  public void setStatuses(String statuses) {
    this.statuses = statuses;
  }

  public Boolean getHasPromotion() {
    return hasPromotion;
  }

  public void setHasPromotion(Boolean hasPromotion) {
    this.hasPromotion = hasPromotion;
  }

  public Integer getPriceFrom() {
    return priceFrom;
  }

  public void setPriceFrom(Integer priceFrom) {
    this.priceFrom = priceFrom;
  }

  public Integer getPriceTo() {
    return priceTo;
  }

  public void setPriceTo(Integer priceTo) {
    this.priceTo = priceTo;
  }

  public String getTypeIds() {
    return typeIds;
  }

  public RoomMapFilterOptionDTO setTypeIds(final String typeIds) {
    this.typeIds = typeIds;
    return this;
  }

  public String getStyleIds() {
    return styleIds;
  }

  public RoomMapFilterOptionDTO setStyleIds(final String styleIds) {
    this.styleIds = styleIds;
    return this;
  }

  @Override
  public String toString() {
    return "RoomMapFilterOptionDTO{"
        + "typeIds='"
        + typeIds
        + '\''
        + ", styleIds='"
        + styleIds
        + '\''
        + ", hasPromotion="
        + hasPromotion
        + ", priceFrom="
        + priceFrom
        + ", priceTo="
        + priceTo
        + ", status="
        + statuses
        + '}';
  }
}