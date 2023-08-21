package vn.lotusviet.hotelmgmt.model.dto.invoice;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class CommonInvoiceFilterDto {
  private InvoiceType invoiceType;
  @NotNull private LocalDate dateFrom;
  @NotNull private LocalDate dateTo;

  public InvoiceType getInvoiceType() {
    return invoiceType;
  }

  public CommonInvoiceFilterDto setInvoiceType(InvoiceType invoiceType) {
    this.invoiceType = invoiceType;
    return this;
  }

  public LocalDate getDateFrom() {
    return dateFrom;
  }

  public CommonInvoiceFilterDto setDateFrom(LocalDate dateFrom) {
    this.dateFrom = dateFrom;
    return this;
  }

  public LocalDate getDateTo() {
    return dateTo;
  }

  public CommonInvoiceFilterDto setDateTo(LocalDate dateTo) {
    this.dateTo = dateTo;
    return this;
  }

  @Override
  public String toString() {
    return "CommonInvoiceFilterDto{"
        + "type="
        + invoiceType
        + ", dateFrom="
        + dateFrom
        + ", dateTo="
        + dateTo
        + '}';
  }
}