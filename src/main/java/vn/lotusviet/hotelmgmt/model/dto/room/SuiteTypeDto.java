package vn.lotusviet.hotelmgmt.model.dto.room;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuiteTypeDto {

  private Integer id;

  @NotBlank private String name;

  @NotNull private List<AmenityDto> amenities;

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

  public List<AmenityDto> getAmenities() {
    return amenities;
  }

  public void setAmenities(List<AmenityDto> amenities) {
    this.amenities = amenities;
  }

  @Override
  public String toString() {
    return "SuiteTypeDto{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", amenities="
        + amenities
        + '}';
  }
}