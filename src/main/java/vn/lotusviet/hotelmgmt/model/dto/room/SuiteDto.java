package vn.lotusviet.hotelmgmt.model.dto.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.util.MathUtil;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class SuiteDto {

  private Integer id;

  private String typeName;

  private String styleName;

  private Integer maxOccupation;

  private Integer area;

  private Integer originalPrice;

  private String imageURL;

  private Integer rentalNum;

  private Integer emptyRoomNum;

  private PromotionDto promotion;

  private Integer vat;

  private List<Integer> amenityIds;

  private List<RoomDto> rooms;

  private Boolean available;

  public Boolean getAvailable() {
    return available;
  }

  public SuiteDto setAvailable(Boolean available) {
    this.available = available;
    return this;
  }

  public List<RoomDto> getRooms() {
    return rooms;
  }

  public SuiteDto setRooms(final List<RoomDto> rooms) {
    this.rooms = rooms;
    return this;
  }

  public int getPromotionDiscountAmount() {
    if (this.promotion == null) return 0;
    return MathUtil.roundHalfThousand(
        this.originalPrice * (this.promotion.getDiscountPercent() / 100.0));
  }

  public Integer getNowDateFinalPrice() {
    if (getOriginalPrice() == null) return null;
    return getOriginalPrice() - getPromotionDiscountAmount();
  }

  @JsonProperty("name")
  public String getName() {
    return getTypeName() + " - " + getStyleName();
  }

  public Integer getVat() {
    return vat;
  }

  public SuiteDto setVat(final Integer vat) {
    this.vat = vat;
    return this;
  }

  public Integer getEmptyRoomNum() {
    return emptyRoomNum;
  }

  public SuiteDto setEmptyRoomNum(final Integer emptyRoomNum) {
    this.emptyRoomNum = emptyRoomNum;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public SuiteDto setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getTypeName() {
    return typeName;
  }

  public SuiteDto setTypeName(final String typeName) {
    this.typeName = typeName;
    return this;
  }

  public String getStyleName() {
    return styleName;
  }

  public SuiteDto setStyleName(final String styleName) {
    this.styleName = styleName;
    return this;
  }

  public Integer getMaxOccupation() {
    return maxOccupation;
  }

  public SuiteDto setMaxOccupation(final Integer maxOccupation) {
    this.maxOccupation = maxOccupation;
    return this;
  }

  public Integer getArea() {
    return area;
  }

  public SuiteDto setArea(final Integer area) {
    this.area = area;
    return this;
  }

  public Integer getOriginalPrice() {
    return originalPrice;
  }

  public SuiteDto setOriginalPrice(final Integer originalPrice) {
    this.originalPrice = originalPrice;
    return this;
  }

  public String getImageURL() {
    return imageURL;
  }

  public SuiteDto setImageURL(final String imageURL) {
    this.imageURL = imageURL;
    return this;
  }

  public Integer getRentalNum() {
    return rentalNum;
  }

  public SuiteDto setRentalNum(final Integer rentalNum) {
    this.rentalNum = rentalNum;
    return this;
  }

  public PromotionDto getPromotion() {
    return promotion;
  }

  public SuiteDto setPromotion(final PromotionDto promotion) {
    this.promotion = promotion;
    return this;
  }

  public List<Integer> getAmenityIds() {
    return amenityIds;
  }

  public SuiteDto setAmenityIds(final List<Integer> amenityIds) {
    this.amenityIds = amenityIds;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SuiteDto)) return false;
    SuiteDto that = (SuiteDto) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "SuiteDto{"
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
        + ", area="
        + area
        + ", originalPrice="
        + originalPrice
        + ", imageURL='"
        + imageURL
        + '\''
        + ", rentalNum="
        + rentalNum
        + ", available="
        + available
        + ", emptyRoomNum="
        + emptyRoomNum
        + ", promotions="
        + promotion
        + ", vat="
        + vat
        + ", amenityIds="
        + amenityIds
        + ", rooms="
        + rooms
        + '}';
  }
}