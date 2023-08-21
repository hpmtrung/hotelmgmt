package vn.lotusviet.hotelmgmt.model.dto.service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ServiceCreateDto {

  @NotNull private Integer serviceTypeId;
  @NotBlank private String name;
  @NotNull private Boolean isActive;
  private Integer unitPrice;
  @NotNull private Integer vat;

  public Integer getServiceTypeId() {
    return serviceTypeId;
  }

  public ServiceCreateDto setServiceTypeId(final Integer serviceTypeId) {
    this.serviceTypeId = serviceTypeId;
    return this;
  }

  public String getName() {
    return name;
  }

  public ServiceCreateDto setName(final String name) {
    this.name = name;
    return this;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public ServiceCreateDto setIsActive(final Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  public Integer getUnitPrice() {
    return unitPrice;
  }

  public ServiceCreateDto setUnitPrice(final Integer unitPrice) {
    this.unitPrice = unitPrice;
    return this;
  }

  public Integer getVat() {
    return vat;
  }

  public ServiceCreateDto setVat(final Integer vat) {
    this.vat = vat;
    return this;
  }

  @Override
  public String toString() {
    return "ServiceCreateDto{"
        + "serviceTypeId='"
        + serviceTypeId
        + '\''
        + ", name='"
        + name
        + '\''
        + ", isActive="
        + isActive
        + ", unitPrice="
        + unitPrice
        + ", vat="
        + vat
        + '}';
  }
}