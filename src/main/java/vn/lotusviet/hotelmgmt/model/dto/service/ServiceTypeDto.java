package vn.lotusviet.hotelmgmt.model.dto.service;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class ServiceTypeDto {

  private Integer id;
  @NotBlank private String name;

  public Integer getId() {
    return id;
  }

  public ServiceTypeDto setId(final Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public ServiceTypeDto setName(final String name) {
    if (name == null || name.isBlank()) throw new IllegalArgumentException();
    this.name = name;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ServiceTypeDto)) return false;
    ServiceTypeDto that = (ServiceTypeDto) o;
    return Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "ServiceTypeDto{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}