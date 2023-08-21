package vn.lotusviet.hotelmgmt.model.entity.rental;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.lotusviet.hotelmgmt.model.entity.ads.Promotion;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PromotionTest {

  private final Promotion promotion = new Promotion();

  @BeforeEach
  void setUp() {
    LocalDate now = LocalDate.now();
    promotion.setStartAt(now).setEndAt(now.plus(1, ChronoUnit.DAYS));
  }

  @Test
  void whenSetInvalidStartAt_thenThrowIAE() {
    assertThrows(IllegalArgumentException.class, () -> promotion.setStartAt(promotion.getEndAt()));
  }

  @Test
  void whenSetInvalidEndAt_thenThrowIAE() {
    assertThrows(IllegalArgumentException.class, () -> promotion.setEndAt(promotion.getStartAt()));
  }
}