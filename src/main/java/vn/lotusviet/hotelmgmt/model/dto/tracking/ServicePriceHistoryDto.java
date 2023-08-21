package vn.lotusviet.hotelmgmt.model.dto.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceDto;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServicePriceHistoryDto {

  private ServiceDto service;
  private LocalDate editedAt;
  private EmployeeDto editedBy;
  private int price;
  private int vat;

  public ServiceDto getService() {
    return service;
  }

  public ServicePriceHistoryDto setService(final ServiceDto service) {
    this.service = service;
    return this;
  }

  public LocalDate getEditedAt() {
    return editedAt;
  }

  public ServicePriceHistoryDto setEditedAt(final LocalDate editedAt) {
    this.editedAt = editedAt;
    return this;
  }

  public EmployeeDto getEditedBy() {
    return editedBy;
  }

  public ServicePriceHistoryDto setEditedBy(final EmployeeDto editedBy) {
    this.editedBy = editedBy;
    return this;
  }

  public int getPrice() {
    return price;
  }

  public ServicePriceHistoryDto setPrice(final int price) {
    this.price = price;
    return this;
  }

  public int getVat() {
    return vat;
  }

  public ServicePriceHistoryDto setVat(final int vat) {
    this.vat = vat;
    return this;
  }

  @Override
  public String toString() {
    return "ServicePriceHistoryDto{"
        + "service="
        + service
        + ", editedAt="
        + editedAt
        + ", editedBy="
        + editedBy
        + ", price="
        + price
        + ", vat="
        + vat
        + '}';
  }
}