package vn.lotusviet.hotelmgmt.model.dto.invoice;

import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;

import java.time.LocalDateTime;

/** Common invoice class used for represent (no-)vat invoice */
public class CommonInvoiceDto {

  private long id;
  private LocalDateTime createdAt;
  private EmployeeDto createdBy;
  private long total;
  private InvoiceType invoiceType;

  public long getId() {
    return id;
  }

  public CommonInvoiceDto setId(long id) {
    this.id = id;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public CommonInvoiceDto setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public EmployeeDto getCreatedBy() {
    return createdBy;
  }

  public CommonInvoiceDto setCreatedBy(EmployeeDto createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public long getTotal() {
    return total;
  }

  public CommonInvoiceDto setTotal(long total) {
    this.total = total;
    return this;
  }

  public InvoiceType getInvoiceType() {
    return invoiceType;
  }

  public CommonInvoiceDto setInvoiceType(InvoiceType invoiceType) {
    this.invoiceType = invoiceType;
    return this;
  }

  @Override
  public String toString() {
    return "CommonInvoiceDto{"
        + "id="
        + id
        + ", createdAt="
        + createdAt
        + ", createdBy="
        + createdBy
        + ", total="
        + total
        + ", type="
        + invoiceType
        + '}';
  }
}