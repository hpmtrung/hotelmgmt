package vn.lotusviet.hotelmgmt.model.dto.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.dto.person.EmployeeDto;
import vn.lotusviet.hotelmgmt.util.DatetimeUtil;

import java.time.LocalDateTime;
import java.util.List;

public class InvoiceDto {

  private long id;
  private EmployeeDto createdBy;
  private String paymentMethodName;
  private LocalDateTime createdAt;
  private int total;
  private List<InvoiceDetailDto> details;
  private int depositAmount;
  private int discountAmount;
  private CustomerDto owner;
  private InvoiceType invoiceType;
  private String customerName;
  private String taxCode;

  @JsonIgnore
  public int getDetailTotal() {
    return details.stream().mapToInt(InvoiceDetailDto::getSubTotalWithNotPaidService).sum();
  }

  public String getCustomerName() {
    return customerName != null ? customerName : owner.getFullName();
  }

  public InvoiceDto setCustomerName(String customerName) {
    this.customerName = customerName;
    return this;
  }

  public String getTaxCode() {
    return taxCode != null ? taxCode : owner.getTaxCode();
  }

  public InvoiceDto setTaxCode(String taxCode) {
    this.taxCode = taxCode;
    return this;
  }

  public InvoiceType getInvoiceType() {
    return invoiceType;
  }

  public InvoiceDto setInvoiceType(InvoiceType invoiceType) {
    this.invoiceType = invoiceType;
    return this;
  }

  public CustomerDto getOwner() {
    return owner;
  }

  public InvoiceDto setOwner(CustomerDto owner) {
    this.owner = owner;
    return this;
  }

  public int getDiscountAmount() {
    return discountAmount;
  }

  public InvoiceDto setDiscountAmount(int discountAmount) {
    this.discountAmount = discountAmount;
    return this;
  }

  public String getPaymentMethodName() {
    return paymentMethodName;
  }

  public void setPaymentMethodName(String paymentMethodName) {
    this.paymentMethodName = paymentMethodName;
  }

  public int getDepositAmount() {
    return depositAmount;
  }

  public InvoiceDto setDepositAmount(int depositAmount) {
    this.depositAmount = depositAmount;
    return this;
  }

  public EmployeeDto getCreatedBy() {
    return createdBy;
  }

  public InvoiceDto setCreatedBy(final EmployeeDto createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public InvoiceDto setCreatedAt(final LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  @JsonIgnore
  public String getCreatedAtString() {
    return DatetimeUtil.formatLocalDateTime(createdAt);
  }

  public int getTotal() {
    return total;
  }

  public InvoiceDto setTotal(final int total) {
    this.total = total;
    return this;
  }

  public List<InvoiceDetailDto> getDetails() {
    return details;
  }

  public InvoiceDto setDetails(final List<InvoiceDetailDto> details) {
    this.details = details;
    return this;
  }

  public long getId() {
    return id;
  }

  public InvoiceDto setId(long id) {
    this.id = id;
    return this;
  }

  @Override
  public String toString() {
    return "InvoiceDto{"
        + "id="
        + id
        + ", createdBy="
        + createdBy
        + ", paymentMethodName='"
        + paymentMethodName
        + '\''
        + ", createdAt="
        + createdAt
        + ", total="
        + total
        + ", details="
        + details
        + ", depositAmount="
        + depositAmount
        + ", discountAmount="
        + discountAmount
        + ", owner="
        + owner
        + ", type="
        + invoiceType
        + '}';
  }
}