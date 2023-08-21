package vn.lotusviet.hotelmgmt.model.dto.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import vn.lotusviet.hotelmgmt.util.DatetimeUtil;

import java.time.LocalDateTime;

public class InvoiceServiceUsageDetailDto {

  private String serviceName;
  private int unitPrice;
  private int quantity;
  private int total;
  private LocalDateTime createdAt;

  @JsonIgnore
  public String getCreatedAtString() {
    return DatetimeUtil.formatLocalDateTime(createdAt);
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public InvoiceServiceUsageDetailDto setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public String getServiceName() {
    return serviceName;
  }

  public InvoiceServiceUsageDetailDto setServiceName(String serviceName) {
    this.serviceName = serviceName;
    return this;
  }

  public int getUnitPrice() {
    return unitPrice;
  }

  public InvoiceServiceUsageDetailDto setUnitPrice(int unitPrice) {
    this.unitPrice = unitPrice;
    return this;
  }

  public int getQuantity() {
    return quantity;
  }

  public InvoiceServiceUsageDetailDto setQuantity(int quantity) {
    this.quantity = quantity;
    return this;
  }

  public int getTotal() {
    return total;
  }

  public InvoiceServiceUsageDetailDto setTotal(int total) {
    this.total = total;
    return this;
  }

  @Override
  public String toString() {
    return "InvoiceServiceUsageDetailDto{"
        + "serviceName='"
        + serviceName
        + '\''
        + ", unitPrice="
        + unitPrice
        + ", quantity="
        + quantity
        + ", total="
        + total
        + '}';
  }
}