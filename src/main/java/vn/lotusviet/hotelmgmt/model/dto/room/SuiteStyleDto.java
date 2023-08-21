package vn.lotusviet.hotelmgmt.model.dto.room;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SuiteStyleDto {

  private Integer id;

  @NotBlank private String name;

  @NotNull private Integer maxOccupation;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getMaxOccupation() {
    return maxOccupation;
  }

  public void setMaxOccupation(Integer maxOccupation) {
    this.maxOccupation = maxOccupation;
  }

  @Override
  public String toString() {
    return "SuiteStyleDto{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", maxOccupation="
        + maxOccupation
        + '}';
  }
}