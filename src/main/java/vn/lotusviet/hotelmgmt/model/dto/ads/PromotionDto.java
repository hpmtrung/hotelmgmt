package vn.lotusviet.hotelmgmt.model.dto.ads;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import vn.lotusviet.hotelmgmt.util.DatetimeUtil;

import java.io.Serializable;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PromotionDto implements Serializable {

  private static final long serialVersionUID = 3667035684782201524L;
  private Long id;
  private String code;
  private Integer discountPercent;
  private LocalDate startAt;
  private LocalDate endAt;
  private Boolean available;

  public Boolean getAvailable() {
    return available;
  }

  public PromotionDto setAvailable(Boolean available) {
    this.available = available;
    return this;
  }

  public Integer getDuration() {
    if (this.startAt == null || this.endAt == null) return null;
    return this.startAt.until(this.endAt).getDays() + 1;
  }

  public Long getId() {
    return id;
  }

  public PromotionDto setId(final Long id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return code;
  }

  public PromotionDto setCode(final String code) {
    this.code = code;
    return this;
  }

  public Integer getDiscountPercent() {
    return discountPercent;
  }

  public PromotionDto setDiscountPercent(final Integer discountPercent) {
    this.discountPercent = discountPercent;
    return this;
  }

  public LocalDate getStartAt() {
    return startAt;
  }

  public PromotionDto setStartAt(final LocalDate startAt) {
    this.startAt = startAt;
    return this;
  }

  public LocalDate getEndAt() {
    return endAt;
  }

  public PromotionDto setEndAt(final LocalDate endAt) {
    this.endAt = endAt;
    return this;
  }

  @JsonIgnore
  public String getEndAtString() {
    return DatetimeUtil.formatLocalDate(this.endAt);
  }

  @JsonIgnore
  public String getStartAtString() {
    return DatetimeUtil.formatLocalDate(this.startAt);
  }

  @Override
  public String toString() {
    return "PromotionDto{"
        + "id="
        + id
        + ", code='"
        + code
        + '\''
        + ", discountPercent="
        + discountPercent
        + ", dateFrom="
        + startAt
        + ", dateTo="
        + endAt
        + ", available="
        + available
        + '}';
  }
}