package vn.lotusviet.hotelmgmt.model.dto.room;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class SuiteTypeUpsertDto {
  @NotBlank private String name;

  @NotNull
  @Size(min = 1)
  private List<Integer> amenityIds;

  public String getName() {
    return name;
  }

  public SuiteTypeUpsertDto setName(String name) {
    this.name = name;
    return this;
  }

  public List<Integer> getAmenityIds() {
    return amenityIds;
  }

  public SuiteTypeUpsertDto setAmenityIds(List<Integer> amenityIds) {
    this.amenityIds = amenityIds;
    return this;
  }

  @Override
  public String toString() {
    return "SuiteTypeUpsertDto{" + "name='" + name + '\'' + ", amenityIds=" + amenityIds + '}';
  }
}