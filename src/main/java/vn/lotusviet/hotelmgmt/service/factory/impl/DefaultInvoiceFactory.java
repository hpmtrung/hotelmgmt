package vn.lotusviet.hotelmgmt.service.factory.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;
import vn.lotusviet.hotelmgmt.model.entity.rental.Invoice;
import vn.lotusviet.hotelmgmt.model.entity.rental.MergedInvoice;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;
import vn.lotusviet.hotelmgmt.service.CommonService;
import vn.lotusviet.hotelmgmt.service.EmployeeService;
import vn.lotusviet.hotelmgmt.service.factory.InvoiceFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Component
public final class DefaultInvoiceFactory implements InvoiceFactory {

  private final CommonService commonService;
  private final EmployeeService employeeService;

  public DefaultInvoiceFactory(CommonService commonService, EmployeeService employeeService) {
    this.commonService = commonService;
    this.employeeService = employeeService;
  }

  @Override
  public Invoice createInvoice(
      @NotNull final LocalDateTime createdAt,
      @NotNull final Rental rental,
      @NotNull final PaymentMethodCode paymentMethodCode) {
    Objects.requireNonNull(createdAt);
    Objects.requireNonNull(rental);
    Objects.requireNonNull(paymentMethodCode);

    return new Invoice()
        .setPaymentMethod(commonService.getPaymentMethodByCode(paymentMethodCode))
        .setCreatedBy(employeeService.getAuditedLogin())
        .setCreatedAt(createdAt)
        .setRental(rental)
        .setDepositUsed(false)
        .setRentalDiscountUsed(false);
  }

  @Override
  public Invoice createVATInvoice(
      @NotNull LocalDateTime createdAt,
      @NotNull Rental rental,
      @NotNull PaymentMethodCode paymentMethodCode,
      @NotNull String customerName,
      @NotNull String taxCode) {
    Objects.requireNonNull(customerName);
    Objects.requireNonNull(taxCode);

    return createInvoice(createdAt, rental, paymentMethodCode)
        .setCustomerName(customerName)
        .setTaxCode(taxCode);
  }

  @Override
  public MergedInvoice createMergedInvoice(
      @NotNull LocalDateTime createdAt, @NotNull Collection<Rental> rentals, long total) {
    Objects.requireNonNull(createdAt);
    Objects.requireNonNull(rentals);

    final MergedInvoice newInvoice =
        new MergedInvoice().setCreatedBy(employeeService.getAuditedLogin()).setCreatedAt(createdAt);

    newInvoice.addRentals(rentals);
    newInvoice.setTotal(total);

    return newInvoice;
  }
}