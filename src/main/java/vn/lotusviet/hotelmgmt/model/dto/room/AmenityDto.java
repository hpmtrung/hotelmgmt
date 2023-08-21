package vn.lotusviet.hotelmgmt.model.dto.room;

import java.util.Objects;

public class AmenityDto {
  private Integer id;
  private String name;

  public Integer getId() {
    return id;
  }

  public AmenityDto setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public AmenityDto setName(final String name) {
    this.name = name;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AmenityDto)) return false;
    AmenityDto that = (AmenityDto) o;
    return Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "AmenityDto{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}