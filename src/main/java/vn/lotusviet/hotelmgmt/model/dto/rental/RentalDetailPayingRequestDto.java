package vn.lotusviet.hotelmgmt.model.dto.rental;

import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceType;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class RentalDetailPayingRequestDto {

  @NotNull private PaymentMethodCode paymentMethodCode;

  @NotNull
  @Size(min = 1)
  private List<Long> rentalDetailIds;

  private boolean rentalDiscountUsed = false;
  private boolean depositUsed = false;
  private RentalPaymentCreateDto payment;
  @NotNull
  private InvoiceType invoiceType;
  private VATCustomerOption vatCustomerOption;

  public InvoiceType getInvoiceType() {
    return invoiceType;
  }

  public RentalDetailPayingRequestDto setInvoiceType(InvoiceType invoiceType) {
    this.invoiceType = invoiceType;
    return this;
  }

  public VATCustomerOption getVatCustomerOption() {
    return vatCustomerOption;
  }

  public RentalDetailPayingRequestDto setVatCustomerOption(VATCustomerOption vatCustomerOption) {
    this.vatCustomerOption = vatCustomerOption;
    return this;
  }

  public RentalPaymentCreateDto getPayment() {
    return payment;
  }

  public RentalDetailPayingRequestDto setPayment(RentalPaymentCreateDto payment) {
    this.payment = payment;
    return this;
  }

  public boolean getRentalDiscountUsed() {
    return rentalDiscountUsed;
  }

  public void setRentalDiscountUsed(boolean rentalDiscountUsed) {
    this.rentalDiscountUsed = rentalDiscountUsed;
  }

  public boolean getDepositUsed() {
    return depositUsed;
  }

  public void setDepositUsed(boolean depositUsed) {
    this.depositUsed = depositUsed;
  }

  public PaymentMethodCode getPaymentMethodCode() {
    return paymentMethodCode;
  }

  public RentalDetailPayingRequestDto setPaymentMethodCode(PaymentMethodCode paymentMethodCode) {
    this.paymentMethodCode = paymentMethodCode;
    return this;
  }

  public List<Long> getRentalDetailIds() {
    return rentalDetailIds;
  }

  public RentalDetailPayingRequestDto setRentalDetailIds(List<Long> rentalDetailIds) {
    this.rentalDetailIds = rentalDetailIds;
    return this;
  }

  @Override
  public String toString() {
    return "RentalDetailPayingRequestDto{"
        + "paymentMethodCode="
        + paymentMethodCode
        + ", rentalDetailIds="
        + rentalDetailIds
        + ", rentalDiscountUsed="
        + rentalDiscountUsed
        + ", depositUsed="
        + depositUsed
        + ", payment="
        + payment
        + ", invoiceType="
        + invoiceType
        + ", vatCustomerOption="
        + vatCustomerOption
        + '}';
  }

  public static class VATCustomerOption {
    private String customerName;
    private String taxCode;

    public String getCustomerName() {
      return customerName;
    }

    public VATCustomerOption setCustomerName(String customerName) {
      this.customerName = customerName;
      return this;
    }

    public String getTaxCode() {
      return taxCode;
    }

    public VATCustomerOption setTaxCode(String taxCode) {
      this.taxCode = taxCode;
      return this;
    }

    @Override
    public String toString() {
      return "VATCustomerOption{"
          + "customerName='"
          + customerName
          + '\''
          + ", taxCode='"
          + taxCode
          + '\''
          + '}';
    }
  }
}