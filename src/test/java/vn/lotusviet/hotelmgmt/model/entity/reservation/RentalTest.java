package vn.lotusviet.hotelmgmt.model.entity.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RentalTest {

  private final Rental rental = new Rental();

  @BeforeEach
  void setUp() {
    final LocalDateTime now = LocalDateTime.now();
    rental
        .setCreatedAt(now)
        .setCheckInAt(now.plus(1, ChronoUnit.DAYS))
        .setCheckOutAt(now.plus(5, ChronoUnit.DAYS));
  }

  @Test
  void whenSetDiscountNegative_thenThrowIAE() {
    assertThrows(IllegalArgumentException.class, () -> rental.setDiscountAmount(-1));
  }

  @Test
  void whenSetCreatedByNull_thenThrowNPE() {
    assertThrows(NullPointerException.class, () -> rental.setCreatedBy(null));
  }

  @Test
  void whenSetCustomerNull_thenThrowNPE() {
    assertThrows(NullPointerException.class, () -> rental.setCreatedBy(null));
  }

  @Test
  void whenAddInvalidDetail_thenThrowException() {
    assertThrows(NullPointerException.class, () -> rental.addDetail(null));
    RentalDetail detail = new RentalDetail();
    rental.addDetail(detail);
    assertThrows(IllegalArgumentException.class, () -> rental.addDetail(detail));
  }

  @Test
  void whenRemoveInvalidDetail_thenThrowException() {
    assertThrows(NullPointerException.class, () -> rental.removeDetail(null));
    RentalDetail detail = new RentalDetail();
    assertThrows(IllegalArgumentException.class, () -> rental.removeDetail(detail));
  }
}