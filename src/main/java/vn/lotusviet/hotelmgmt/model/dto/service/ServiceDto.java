package vn.lotusviet.hotelmgmt.model.dto.service;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceDto {

  private Integer id;
  private Integer serviceTypeId;
  private String name;
  private boolean isActive;
  private Integer unitPrice;
  private Integer vat;

  public Integer getId() {
    return id;
  }

  public ServiceDto setId(final Integer id) {
    this.id = id;
    return this;
  }

  public Integer getServiceTypeId() {
    return serviceTypeId;
  }

  public ServiceDto setServiceTypeId(final Integer serviceTypeId) {
    this.serviceTypeId = serviceTypeId;
    return this;
  }

  public String getName() {
    return name;
  }

  public ServiceDto setName(final String name) {
    this.name = name;
    return this;
  }

  public boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(boolean active) {
    isActive = active;
  }

  public Integer getUnitPrice() {
    return unitPrice;
  }

  public ServiceDto setUnitPrice(final Integer unitPrice) {
    this.unitPrice = unitPrice;
    return this;
  }

  public Integer getVat() {
    return vat;
  }

  public ServiceDto setVat(final Integer vat) {
    this.vat = vat;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ServiceDto)) return false;
    ServiceDto that = (ServiceDto) o;
    return Objects.equals(getName(), that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName());
  }

  @Override
  public String toString() {
    return "ServiceDto{"
        + "id="
        + id
        + ", serviceType="
        + serviceTypeId
        + ", name='"
        + name
        + '\''
        + ", isActive="
        + isActive
        + ", price="
        + unitPrice
        + ", vat="
        + vat
        + '}';
  }
}