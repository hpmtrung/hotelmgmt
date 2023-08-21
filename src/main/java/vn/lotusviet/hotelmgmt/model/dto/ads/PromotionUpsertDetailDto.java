package vn.lotusviet.hotelmgmt.model.dto.ads;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class PromotionUpsertDetailDto implements Serializable {

  private static final long serialVersionUID = -4918133387707324775L;

  @NotNull
  private Integer suiteId;
  @NotNull
  private Integer discountPercent;

  public Integer getSuiteId() {
    return suiteId;
  }

  public PromotionUpsertDetailDto setSuiteId(final Integer suiteId) {
    this.suiteId = suiteId;
    return this;
  }

  public Integer getDiscountPercent() {
    return discountPercent;
  }

  public PromotionUpsertDetailDto setDiscountPercent(final Integer discountPercent) {
    this.discountPercent = discountPercent;
    return this;
  }

  @Override
  public String toString() {
    return "PromotionUpsertDetailDto{"
        + "suiteId="
        + suiteId
        + ", discountPercent="
        + discountPercent
        + '}';
  }
}