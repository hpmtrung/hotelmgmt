package vn.lotusviet.hotelmgmt.model.dto.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.util.MathUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SuiteReservationDto {

  private int id;

  private String typeName;

  private String styleName;

  private int maxOccupation;

  private int area;

  private int originalPrice;

  private String imageURL;

  private int rentalNum;

  private int emptyRoomNum;

  private List<PromotionDto> promotions = new ArrayList<>();

  private int vat;

  private List<Integer> amenityIds;

  public int getNowDateFinalPrice() {
    int result = getOriginalPrice();
    final LocalDate now = LocalDate.now();
    int discountPercent =
        promotions.stream()
            .filter(p -> !p.getStartAt().isAfter(now) && !p.getEndAt().isBefore(now))
            .findFirst()
            .map(PromotionDto::getDiscountPercent)
            .orElse(0);
    return MathUtil.roundHalfThousand(result * (1 - discountPercent / 100.0));
  }

  public int getPromotionDiscountTotal() {
    return this.promotions.stream()
        .mapToInt(
            p ->
                MathUtil.roundHalfThousand(
                    p.getDuration() * this.originalPrice * (p.getDiscountPercent() / 100.0)))
        .sum();
  }

  @JsonProperty("name")
  public String getName() {
    return getTypeName() + " - " + getStyleName();
  }

  public int getId() {
    return id;
  }

  public SuiteReservationDto setId(final int id) {
    this.id = id;
    return this;
  }

  public String getTypeName() {
    return typeName;
  }

  public SuiteReservationDto setTypeName(final String typeName) {
    this.typeName = typeName;
    return this;
  }

  public String getStyleName() {
    return styleName;
  }

  public SuiteReservationDto setStyleName(final String styleName) {
    this.styleName = styleName;
    return this;
  }

  public int getMaxOccupation() {
    return maxOccupation;
  }

  public SuiteReservationDto setMaxOccupation(final int maxOccupation) {
    this.maxOccupation = maxOccupation;
    return this;
  }

  public int getArea() {
    return area;
  }

  public SuiteReservationDto setArea(final int area) {
    this.area = area;
    return this;
  }

  public int getOriginalPrice() {
    return originalPrice;
  }

  public SuiteReservationDto setOriginalPrice(final int originalPrice) {
    this.originalPrice = originalPrice;
    return this;
  }

  public String getImageURL() {
    return imageURL;
  }

  public SuiteReservationDto setImageURL(final String imageURL) {
    this.imageURL = imageURL;
    return this;
  }

  public int getRentalNum() {
    return rentalNum;
  }

  public SuiteReservationDto setRentalNum(final int rentalNum) {
    this.rentalNum = rentalNum;
    return this;
  }

  public int getEmptyRoomNum() {
    return emptyRoomNum;
  }

  public SuiteReservationDto setEmptyRoomNum(final int emptyRoomNum) {
    this.emptyRoomNum = emptyRoomNum;
    return this;
  }

  public List<PromotionDto> getPromotions() {
    return promotions;
  }

  public SuiteReservationDto setPromotions(final List<PromotionDto> promotions) {
    this.promotions = promotions;
    return this;
  }

  public int getVat() {
    return vat;
  }

  public SuiteReservationDto setVat(final int vat) {
    this.vat = vat;
    return this;
  }

  public List<Integer> getAmenityIds() {
    return amenityIds;
  }

  public SuiteReservationDto setAmenityIds(final List<Integer> amenityIds) {
    this.amenityIds = amenityIds;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SuiteReservationDto)) return false;
    SuiteReservationDto that = (SuiteReservationDto) o;
    return getId() == that.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }

  @Override
  public String toString() {
    return "SuiteReservationDto{"
        + "id="
        + id
        + ", typeName='"
        + typeName
        + '\''
        + ", styleName='"
        + styleName
        + '\''
        + ", maxOccupation="
        + maxOccupation
        + ", originalPrice="
        + originalPrice
        + ", promotions="
        + promotions
        + '}';
  }
}