package vn.lotusviet.hotelmgmt.model.dto.ads;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

public class PromotionCreateDto implements Serializable {

  private static final long serialVersionUID = -4007100750386622289L;

  @NotBlank private String code;
  @NotNull private LocalDate startAt;
  @NotNull private LocalDate endAt;

  public String getCode() {
    return code;
  }

  public PromotionCreateDto setCode(final String code) {
    this.code = code;
    return this;
  }

  public LocalDate getStartAt() {
    return startAt;
  }

  public PromotionCreateDto setStartAt(final LocalDate startAt) {
    this.startAt = startAt;
    return this;
  }

  public LocalDate getEndAt() {
    return endAt;
  }

  public PromotionCreateDto setEndAt(final LocalDate endAt) {
    this.endAt = endAt;
    return this;
  }

  @Override
  public String toString() {
    return "PromotionCreateDto{"
        + "code='"
        + code
        + '\''
        + ", startAt="
        + startAt
        + ", endAt="
        + endAt
        + '}';
  }
}