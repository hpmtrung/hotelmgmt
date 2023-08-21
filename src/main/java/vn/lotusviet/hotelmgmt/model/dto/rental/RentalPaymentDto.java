package vn.lotusviet.hotelmgmt.model.dto.rental;

import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;

import java.time.LocalDateTime;

public class RentalPaymentDto {
  private long id;
  private LocalDateTime createdAt;
  private int money;
  private String content;
  private EmployeeDto createdBy;

  public long getId() {
    return id;
  }

  public RentalPaymentDto setId(long id) {
    this.id = id;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public RentalPaymentDto setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public int getMoney() {
    return money;
  }

  public RentalPaymentDto setMoney(int money) {
    this.money = money;
    return this;
  }

  public String getContent() {
    return content;
  }

  public RentalPaymentDto setContent(String content) {
    this.content = content;
    return this;
  }

  public EmployeeDto getCreatedBy() {
    return createdBy;
  }

  public RentalPaymentDto setCreatedBy(EmployeeDto createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  @Override
  public String toString() {
    return "RentalPaymentDto{"
        + "id="
        + id
        + ", createdAt="
        + createdAt
        + ", money="
        + money
        + ", content='"
        + content
        + '\''
        + ", createdBy="
        + createdBy
        + '}';
  }
}