package vn.lotusviet.hotelmgmt.model.entity.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RentalRoomMappingTest {

  private final RentalDetail detail = new RentalDetail();

  @BeforeEach
  void setUp() {
    final LocalDateTime now = LocalDateTime.now();
    detail.setCheckInAt(now).setCheckOutAt(now.plus(1, ChronoUnit.DAYS));
  }

  @Test
  void whenSetRoomNull_thenThrowNPE() {
    assertThrows(NullPointerException.class, () -> detail.setRoom(null));
  }

  @Test
  void whenSetInvalidRoomPrice_thenThrowIAE() {
    assertThrows(IllegalArgumentException.class, () -> detail.setRoomPrice(-1));
    assertThrows(IllegalArgumentException.class, () -> detail.setRoomPrice(0));
  }

  @Test
  void whenSetDiscountNegative_thenThrowIAE() {
    assertThrows(IllegalArgumentException.class, () -> detail.setDiscountAmount(-2));
  }

  @Test
  void whenSetExtraNegative_thenThrowIAE() {
    assertThrows(IllegalArgumentException.class, () -> detail.setExtraAmount(-2));
  }

  @Test
  void whenSetInvalidTotal_thenThrowIAE() {
    assertThrows(IllegalArgumentException.class, () -> detail.setSubTotal(-1));
    assertThrows(IllegalArgumentException.class, () -> detail.setSubTotal(0));
  }

  @Test
  void whenAddNullCustomer_thenThrowNPE() {
    assertThrows(NullPointerException.class, () -> detail.addCustomer(null));
  }

  @Test
  void whenAddNullServiceDetail_thenThrowNPE() {
    assertThrows(NullPointerException.class, () -> detail.addServiceUsage(null));
  }
}