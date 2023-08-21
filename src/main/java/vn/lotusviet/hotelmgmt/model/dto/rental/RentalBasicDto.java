package vn.lotusviet.hotelmgmt.model.dto.rental;

import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatusCode;

import java.time.LocalDateTime;

public class RentalBasicDto {

  private long id;
  private LocalDateTime createdAt;
  private LocalDateTime checkInAt;
  private LocalDateTime checkOutAt;
  private int discountAmount;
  private CustomerDto owner;
  private RentalStatusCode status;
  private EmployeeDto createdBy;

  public long getId() {
    return id;
  }

  public RentalBasicDto setId(long id) {
    this.id = id;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public RentalBasicDto setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getCheckInAt() {
    return checkInAt;
  }

  public RentalBasicDto setCheckInAt(LocalDateTime checkInAt) {
    this.checkInAt = checkInAt;
    return this;
  }

  public LocalDateTime getCheckOutAt() {
    return checkOutAt;
  }

  public RentalBasicDto setCheckOutAt(LocalDateTime checkOutAt) {
    this.checkOutAt = checkOutAt;
    return this;
  }

  public int getDiscountAmount() {
    return discountAmount;
  }

  public RentalBasicDto setDiscountAmount(int discountAmount) {
    this.discountAmount = discountAmount;
    return this;
  }

  public CustomerDto getOwner() {
    return owner;
  }

  public RentalBasicDto setOwner(CustomerDto owner) {
    this.owner = owner;
    return this;
  }

  public RentalStatusCode getStatus() {
    return status;
  }

  public RentalBasicDto setStatus(RentalStatusCode status) {
    this.status = status;
    return this;
  }

  public EmployeeDto getCreatedBy() {
    return createdBy;
  }

  public RentalBasicDto setCreatedBy(EmployeeDto createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  @Override
  public String toString() {
    return "RentalBasicDto{"
        + "id="
        + id
        + ", createdAt="
        + createdAt
        + ", checkInAt="
        + checkInAt
        + ", checkOutAt="
        + checkOutAt
        + ", discountAmount="
        + discountAmount
        + ", owner="
        + owner
        + ", createdBy="
        + createdBy
        + '}';
  }
}