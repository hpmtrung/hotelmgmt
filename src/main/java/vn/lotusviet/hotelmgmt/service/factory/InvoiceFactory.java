package vn.lotusviet.hotelmgmt.service.factory;

import org.jetbrains.annotations.NotNull;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;
import vn.lotusviet.hotelmgmt.model.entity.rental.Invoice;
import vn.lotusviet.hotelmgmt.model.entity.rental.MergedInvoice;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;

import java.time.LocalDateTime;
import java.util.Collection;

public interface InvoiceFactory {

  Invoice createInvoice(
      @NotNull LocalDateTime createdAt,
      @NotNull Rental rental,
      @NotNull PaymentMethodCode paymentMethodCode);

  Invoice createVATInvoice(
      @NotNull LocalDateTime createdAt,
      @NotNull Rental rental,
      @NotNull PaymentMethodCode paymentMethodCode,
      @NotNull String customerName,
      @NotNull String taxCode);

  MergedInvoice createMergedInvoice(
      @NotNull LocalDateTime createdAt, @NotNull Collection<Rental> rentals, long total);
}