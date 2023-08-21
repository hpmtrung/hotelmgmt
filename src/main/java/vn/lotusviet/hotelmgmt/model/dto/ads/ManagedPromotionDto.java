package vn.lotusviet.hotelmgmt.model.dto.ads;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagedPromotionDto implements Serializable {
  private static final long serialVersionUID = -8667626576775999824L;

  private Long id;
  private String code;
  private LocalDate startAt;
  private LocalDate endAt;
  private Boolean available;
  private List<AppliedSuiteDto> appliedSuites = new ArrayList<>();

  public List<AppliedSuiteDto> getAppliedSuites() {
    return appliedSuites;
  }

  public void setAppliedSuites(List<AppliedSuiteDto> appliedSuiteDtos) {
    this.appliedSuites = appliedSuiteDtos;
  }

  public Boolean getAvailable() {
    return available;
  }

  public ManagedPromotionDto setAvailable(Boolean available) {
    this.available = available;
    return this;
  }

  public LocalDate getStartAt() {
    return startAt;
  }

  public ManagedPromotionDto setStartAt(final LocalDate startAt) {
    this.startAt = startAt;
    return this;
  }

  public LocalDate getEndAt() {
    return endAt;
  }

  public ManagedPromotionDto setEndAt(final LocalDate endAt) {
    this.endAt = endAt;
    return this;
  }

  public Long getId() {
    return id;
  }

  public ManagedPromotionDto setId(final Long id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return code;
  }

  public ManagedPromotionDto setCode(final String code) {
    this.code = code;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ManagedPromotionDto)) return false;
    ManagedPromotionDto that = (ManagedPromotionDto) o;
    return code.equals(that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  @Override
  public String toString() {
    return "ManagedPromotionDto{"
        + "id="
        + id
        + ", code='"
        + code
        + '\''
        + ", startAt="
        + startAt
        + ", endAt="
        + endAt
        + ", available="
        + available
        + ", appliedSuites="
        + appliedSuites
        + '}';
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class AppliedSuiteDto implements Serializable {

    private static final long serialVersionUID = -8198075770127627717L;

    private SuiteDto suite;
    private Integer discountPercent;

    public SuiteDto getSuite() {
      return suite;
    }

    public void setSuite(SuiteDto suite) {
      this.suite = suite;
    }

    public Integer getDiscountPercent() {
      return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
      this.discountPercent = discountPercent;
    }

    @Override
    public String toString() {
      return "AppliedSuiteDto{" + "suite=" + suite + ", discountPercent=" + discountPercent + '}';
    }
  }
}