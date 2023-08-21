package vn.lotusviet.hotelmgmt.model.entity.reservation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationRoomMappingTest {

  private final ReservationDetail detail = new ReservationDetail();

  @Test
  void whenSetInvalidRoomNum_thenThrowIAE() {
    assertThrows(IllegalArgumentException.class, () -> detail.setRoomNum(-1));
    assertThrows(IllegalArgumentException.class, () -> detail.setRoomNum(0));
  }

  @Test
  void whenSetInvalidSuitePrice_thenThrowIAE() {
    assertThrows(IllegalArgumentException.class, () -> detail.setSuitePrice(0));
    assertThrows(IllegalArgumentException.class, () -> detail.setSuitePrice(-1));
  }

}