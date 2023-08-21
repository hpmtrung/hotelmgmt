package vn.lotusviet.hotelmgmt.model.dto.room;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RoomUpsertDto {

  private Integer id;
  @NotBlank private String name;
  @NotNull private Integer floor;
  private Boolean available;

  public Boolean getAvailable() {
    return available;
  }

  public RoomUpsertDto setAvailable(Boolean available) {
    this.available = available;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public RoomUpsertDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public RoomUpsertDto setName(String name) {
    this.name = name;
    return this;
  }

  public Integer getFloor() {
    return floor;
  }

  public RoomUpsertDto setFloor(Integer floor) {
    this.floor = floor;
    return this;
  }

  @Override
  public String toString() {
    return "RoomUpsertDto{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", floor="
        + floor
        + ", available="
        + available
        + '}';
  }
}