package vn.lotusviet.hotelmgmt.model.entity.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationTest {

  private final Reservation reservation = new Reservation();

  @BeforeEach
  void setUp() {
    final Instant now = Instant.now();
    reservation
        .setCreatedAt(now)
        .setCheckInAt(now.atZone(ZoneOffset.UTC).toLocalDate())
        .setCheckOutAt(now.plus(1, ChronoUnit.DAYS).atZone(ZoneOffset.UTC).toLocalDate());
  }

  @Test
  void whenSetDepositNegative_thenThrowIAE() {
    assertThrows(IllegalArgumentException.class, () -> reservation.setDepositAmount(-1));
  }

  @Test
  void whenSetNegativeFee_thenThrowIAE() {
    assertThrows(IllegalArgumentException.class, () -> reservation.setFee(-1));
  }

  @Test
  void whenSetNegativeDeposit_thenThrowIAE() {
    assertThrows(IllegalArgumentException.class, () -> reservation.setDepositAmount(-1));
  }

  @Test
  void whenSetNullStatus_thenThrowIAE() {
    assertThrows(NullPointerException.class, () -> reservation.setStatus(null));
  }

  @Test
  void whenAddInvalidDetail_thenThrowException() {
    assertThrows(NullPointerException.class, () -> reservation.addDetail(null));

    ReservationDetail detail =
        new ReservationDetail().setReservation(new Reservation()).setSuite(new Suite());
    reservation.addDetail(detail);
    assertThrows(IllegalArgumentException.class, () -> reservation.addDetail(detail));
  }

  @Test
  void whenRemoveInvalidDetail_thenThrowException() {
    assertThrows(NullPointerException.class, () -> reservation.removeDetail(null));

    ReservationDetail detail =
        new ReservationDetail().setReservation(new Reservation()).setSuite(new Suite());
    assertThrows(IllegalArgumentException.class, () -> reservation.removeDetail(detail));
  }
}