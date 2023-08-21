package vn.lotusviet.hotelmgmt.model.dto.person;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentDto {

  private Integer id;
  private String name;
  private String hotline;

  public Integer getId() {
    return id;
  }

  public DepartmentDto setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public DepartmentDto setName(final String name) {
    this.name = name;
    return this;
  }

  public String getHotline() {
    return hotline;
  }

  public DepartmentDto setHotline(final String hotline) {
    this.hotline = hotline;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DepartmentDto)) return false;
    DepartmentDto that = (DepartmentDto) o;
    return Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "DepartmentDto{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", hotline='"
        + hotline
        + '\''
        + '}';
  }
}