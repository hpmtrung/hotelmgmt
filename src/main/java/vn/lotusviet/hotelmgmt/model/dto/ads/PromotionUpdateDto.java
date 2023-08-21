package vn.lotusviet.hotelmgmt.model.dto.ads;

import java.io.Serializable;
import java.util.List;

public class PromotionUpdateDto implements Serializable {

  private static final long serialVersionUID = 4270548088054406043L;

  private String code;
  private Boolean available;
  private List<PromotionUpsertDetailDto> addedDetails;

  public Boolean getAvailable() {
    return available;
  }

  public PromotionUpdateDto setAvailable(Boolean available) {
    this.available = available;
    return this;
  }

  public String getCode() {
    return code;
  }

  public PromotionUpdateDto setCode(final String code) {
    this.code = code;
    return this;
  }

  public List<PromotionUpsertDetailDto> getAddedDetails() {
    return addedDetails;
  }

  public PromotionUpdateDto setAddedDetails(final List<PromotionUpsertDetailDto> addedDetails) {
    this.addedDetails = addedDetails;
    return this;
  }

  @Override
  public String toString() {
    return "PromotionUpdateDto{"
        + "code='"
        + code
        + '\''
        + ", available="
        + available
        + ", addedDetails="
        + addedDetails
        + '}';
  }
}