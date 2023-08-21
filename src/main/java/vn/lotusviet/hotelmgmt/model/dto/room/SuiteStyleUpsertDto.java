package vn.lotusviet.hotelmgmt.model.dto.room;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SuiteStyleUpsertDto {

  @NotBlank private String name;

  @NotNull @Positive private Integer maxOccupation;

  public String getName() {
    return name;
  }

  public SuiteStyleUpsertDto setName(String name) {
    this.name = name;
    return this;
  }

  public Integer getMaxOccupation() {
    return maxOccupation;
  }

  public SuiteStyleUpsertDto setMaxOccupation(Integer maxOccupation) {
    this.maxOccupation = maxOccupation;
    return this;
  }

  @Override
  public String toString() {
    return "SuiteStyleUpsertDto{"
        + "name='"
        + name
        + '\''
        + ", maxOccupation="
        + maxOccupation
        + '}';
  }
}