package vn.lotusviet.hotelmgmt.model.dto.service;

import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;

import java.time.LocalDateTime;

public class ServiceUsageDetailDto {

  private long id;
  private long rentalDetailId;
  private ServiceDto service;
  private EmployeeDto createdBy;
  private LocalDateTime createdAt;
  private int quantity;
  private int total;
  private boolean isPaid;
  private int vat;

  public long getRentalDetailId() {
    return rentalDetailId;
  }

  public void setRentalDetailId(long rentalDetailId) {
    this.rentalDetailId = rentalDetailId;
  }

  public int getVat() {
    return vat;
  }

  public void setVat(int vat) {
    this.vat = vat;
  }

  public boolean getIsPaid() {
    return isPaid;
  }

  public ServiceUsageDetailDto setIsPaid(final boolean isPaid) {
    this.isPaid = isPaid;
    return this;
  }

  public long getId() {
    return id;
  }

  public ServiceUsageDetailDto setId(final long id) {
    this.id = id;
    return this;
  }

  public ServiceDto getService() {
    return service;
  }

  public ServiceUsageDetailDto setService(final ServiceDto service) {
    this.service = service;
    return this;
  }

  public EmployeeDto getCreatedBy() {
    return createdBy;
  }

  public ServiceUsageDetailDto setCreatedBy(final EmployeeDto createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public ServiceUsageDetailDto setCreatedAt(final LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public int getQuantity() {
    return quantity;
  }

  public ServiceUsageDetailDto setQuantity(final int quantity) {
    this.quantity = quantity;
    return this;
  }

  public int getTotal() {
    return total;
  }

  public ServiceUsageDetailDto setTotal(final int total) {
    this.total = total;
    return this;
  }

  @Override
  public String toString() {
    return "ServiceUsageDetailDto{"
        + "id="
        + id
        + ", service="
        + service
        + ", createdBy="
        + createdBy
        + ", registeredAt="
        + createdAt
        + ", quanity="
        + quantity
        + ", total="
        + total
        + ", isPaid="
        + isPaid
        + '}';
  }
}