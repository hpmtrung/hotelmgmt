package vn.lotusviet.hotelmgmt.model.dto.service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ServiceUpdateDto {

  private Integer serviceTypeId;
  @NotBlank private String name;
  @NotNull private Boolean isActive;

  public ServiceUpdateDto setIsActive(final Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  public Integer getServiceTypeId() {
    return serviceTypeId;
  }

  public ServiceUpdateDto setServiceTypeId(final Integer serviceTypeId) {
    this.serviceTypeId = serviceTypeId;
    return this;
  }

  public String getName() {
    return name;
  }

  public ServiceUpdateDto setName(final String name) {
    this.name = name;
    return this;
  }

  public Boolean getActive() {
    return isActive;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }

  @Override
  public String toString() {
    return "ServiceUpdateDto{"
        + "serviceTypeId="
        + serviceTypeId
        + ", name='"
        + name
        + '\''
        + ", isActive="
        + isActive
        + '}';
  }
}