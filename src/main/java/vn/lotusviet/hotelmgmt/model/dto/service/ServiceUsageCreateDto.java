package vn.lotusviet.hotelmgmt.model.dto.service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

public class ServiceUsageCreateDto {

  @NotNull
  @Size(min = 1)
  private List<Item> items;

  public List<Item> getItems() {
    return items;
  }

  public ServiceUsageCreateDto setItems(List<Item> items) {
    this.items = items;
    return this;
  }

  @Override
  public String toString() {
    return "ServiceUsageCreateDto{" + "items=" + items + '}';
  }

  public static class Item {

    @NotNull private Integer serviceId;

    @NotNull @Positive private Integer quantity;

    @NotNull @Positive private Integer unitPrice;

    @NotNull private Boolean isPaid;

    public Integer getUnitPrice() {
      return unitPrice;
    }

    public Item setUnitPrice(Integer unitPrice) {
      this.unitPrice = unitPrice;
      return this;
    }

    public Integer getServiceId() {
      return serviceId;
    }

    public Item setServiceId(final Integer serviceId) {
      this.serviceId = serviceId;
      return this;
    }

    public Integer getQuantity() {
      return quantity;
    }

    public Item setQuantity(final Integer quantity) {
      this.quantity = quantity;
      return this;
    }

    public Boolean getIsPaid() {
      return isPaid;
    }

    public Item setIsPaid(final Boolean isPaid) {
      this.isPaid = isPaid;
      return this;
    }

    @Override
    public String toString() {
      return "AppliedSuiteDto{"
          + "serviceId="
          + serviceId
          + ", quantity="
          + quantity
          + ", isPaid="
          + isPaid
          + '}';
    }
  }
}