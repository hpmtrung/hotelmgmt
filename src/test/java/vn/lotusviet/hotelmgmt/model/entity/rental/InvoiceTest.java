package vn.lotusviet.hotelmgmt.model.entity.rental;

import org.junit.jupiter.api.Test;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceUsageDetail;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {

  private final Invoice invoice = new Invoice();

  @Test
  void whenSetNullRental_thenThrowNPE() {
    assertThrows(NullPointerException.class, () -> invoice.setRental(null));
  }

  @Test
  void whenSetInvalidTotal_thenThrowIAE() {
    assertThrows(IllegalArgumentException.class, () -> invoice.setTotal(-1));
    assertThrows(IllegalArgumentException.class, () -> invoice.setTotal(0));
  }

  @Test
  void whenSetNullCreatedBy_thenThrowNPE() {
    assertThrows(NullPointerException.class, () -> invoice.setCreatedBy(null));
  }

  @Test
  void whenSetNullPaymentMethod_thenThrowNPE() {
    assertThrows(NullPointerException.class, () -> invoice.setPaymentMethod(null));
  }

  @Test
  void whenSetNullCreatedAt_thenThrowNPE() {
    assertThrows(NullPointerException.class, () -> invoice.setCreatedAt(null));
  }

  @Test
  void whenAddInvalidRentalDetail_thenThrowException() {
    assertThrows(NullPointerException.class, () -> invoice.addRentalDetail(null));

    RentalDetail detail = new RentalDetail();
    invoice.addRentalDetail(detail);
    assertThrows(IllegalArgumentException.class, () -> invoice.addRentalDetail(detail));
  }

  @Test
  void whenAddInvalidServiceUsageDetail_thenThrowException() {
    assertThrows(NullPointerException.class, () -> invoice.addServiceUsage(null));

    ServiceUsageDetail detail = new ServiceUsageDetail();
    invoice.addServiceUsage(detail);
    assertThrows(IllegalArgumentException.class, () -> invoice.addServiceUsage(detail));
  }

  @Test
  void whenAddValidRentalDetail_thenAssociationIsCorrect() {
    RentalDetail detail = new RentalDetail();
    invoice.addRentalDetail(detail);

    assertTrue(invoice.getRentalDetails().contains(detail));
    assertEquals(detail.getInvoice(), invoice);
  }

  @Test
  void whenAddValidServiceUsage_thenAssociationIsCorrect() {
    ServiceUsageDetail detail = new ServiceUsageDetail();
    invoice.addServiceUsage(detail);

    assertTrue(invoice.getServiceUsageDetails().contains(detail));
    assertEquals(detail.getInvoice(), invoice);
  }
}