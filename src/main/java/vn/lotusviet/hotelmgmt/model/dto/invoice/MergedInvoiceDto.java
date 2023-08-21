package vn.lotusviet.hotelmgmt.model.dto.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;
import vn.lotusviet.hotelmgmt.util.DatetimeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class MergedInvoiceDto {
  private long id;
  private EmployeeDto createdBy;
  private LocalDateTime createdAt;
  private long total;
  private List<MergedInvoiceRentalDto> rentals;

  public List<MergedInvoiceRentalDto> getRentals() {
    return rentals;
  }

  public MergedInvoiceDto setRentals(List<MergedInvoiceRentalDto> rentals) {
    this.rentals = Objects.requireNonNull(rentals);
    return this;
  }

  public long getId() {
    return id;
  }

  public MergedInvoiceDto setId(long id) {
    this.id = id;
    return this;
  }

  @JsonIgnore
  public String getCreatedAtString() {
    return DatetimeUtil.formatLocalDateTime(createdAt);
  }

  public EmployeeDto getCreatedBy() {
    return createdBy;
  }

  public MergedInvoiceDto setCreatedBy(EmployeeDto createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public MergedInvoiceDto setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public long getTotal() {
    return total;
  }

  public MergedInvoiceDto setTotal(long total) {
    this.total = total;
    return this;
  }

  @Override
  public String toString() {
    return "MergedInvoiceDto{"
        + "id="
        + id
        + ", createdBy="
        + createdBy
        + ", createdAt="
        + createdAt
        + ", total="
        + total
        + '}';
  }

  public static class MergedInvoiceRentalDto {
    private long id;
    private LocalDateTime createdAt;
    private LocalDateTime checkInAt;
    private LocalDateTime checkOutAt;
    private int total;

    @JsonIgnore
    public String getCreatedAtStr() {
      return DatetimeUtil.formatLocalDateTime(createdAt);
    }

    @JsonIgnore
    public String getCheckInAtStr() {
      return DatetimeUtil.formatLocalDateTime(checkInAt);
    }

    @JsonIgnore
    public String getCheckOutAtStr() {
      return DatetimeUtil.formatLocalDateTime(checkOutAt);
    }

    public long getId() {
      return id;
    }

    public MergedInvoiceRentalDto setId(long id) {
      this.id = id;
      return this;
    }

    public LocalDateTime getCreatedAt() {
      return createdAt;
    }

    public MergedInvoiceRentalDto setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public LocalDateTime getCheckInAt() {
      return checkInAt;
    }

    public MergedInvoiceRentalDto setCheckInAt(LocalDateTime checkInAt) {
      this.checkInAt = checkInAt;
      return this;
    }

    public LocalDateTime getCheckOutAt() {
      return checkOutAt;
    }

    public MergedInvoiceRentalDto setCheckOutAt(LocalDateTime checkOutAt) {
      this.checkOutAt = checkOutAt;
      return this;
    }

    public int getTotal() {
      return total;
    }

    public MergedInvoiceRentalDto setTotal(int total) {
      this.total = total;
      return this;
    }

    @Override
    public String toString() {
      return "MergedInvoiceRentalDto{"
          + "id="
          + id
          + ", createdAt="
          + createdAt
          + ", checkInAt="
          + checkInAt
          + ", checkOutAt="
          + checkOutAt
          + ", total="
          + total
          + '}';
    }
  }
}