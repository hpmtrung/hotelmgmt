package vn.lotusviet.hotelmgmt.util;

import org.junit.jupiter.api.Test;
import vn.lotusviet.hotelmgmt.util.DatetimeUtil.DateRange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatetimeUtilTest {

  @Test
  void WhenGetStartOfDate_thenResultIsCorrect() {
    LocalDate input = LocalDate.now();
    LocalDateTime output = DatetimeUtil.atStartOfDate(input);
    assertEquals(input, output.toLocalDate());
    assertEquals(LocalTime.of(0, 0, 0), LocalTime.from(output).truncatedTo(ChronoUnit.SECONDS));
  }

  @Test
  void WhenGetEndOfDate_thenResultIsCorrect() {
    LocalDate input = LocalDate.now();
    LocalDateTime output = DatetimeUtil.atEndOfDate(input);
    assertEquals(input, output.toLocalDate());
    assertEquals(LocalTime.of(23, 59, 59), LocalTime.from(output).truncatedTo(ChronoUnit.SECONDS));
  }

  @Test
  void whenGetStartOfMonth_thenResultIsCorrect() {
    LocalDate input = LocalDate.of(2022, 1, 12);
    LocalDate output = DatetimeUtil.atStartOfMonth(input);
    assertEquals(1, output.getDayOfMonth());
    assertEquals(input.getMonth(), output.getMonth());
    assertEquals(input.getYear(), output.getYear());
  }

  @Test
  void whenGetMidOfDate_thenResultIsCorrect() {
    LocalDate dateInput = LocalDate.now();
    LocalDateTime output = DatetimeUtil.atMidOfDate(dateInput);

    assertEquals(dateInput, output.toLocalDate());
    assertEquals(LocalTime.of(12, 0, 0), output.toLocalTime().truncatedTo(ChronoUnit.SECONDS));

    LocalDateTime datetimeInput = LocalDateTime.now();
    output = DatetimeUtil.atMidOfDate(datetimeInput);

    assertEquals(datetimeInput.toLocalDate(), output.toLocalDate());
    assertEquals(LocalTime.of(12, 0, 0), output.toLocalTime().truncatedTo(ChronoUnit.SECONDS));
  }

  @Test
  void whenGetEndOfMonth_thenResultIsCorrect() {
    LocalDate input = LocalDate.of(2022, 1, 12);
    LocalDate output = DatetimeUtil.atEndOfMonth(input);
    assertEquals(31, output.getDayOfMonth());
    assertEquals(input.getMonth(), output.getMonth());
    assertEquals(input.getYear(), output.getYear());
  }

  @Test
  void whenGetIntersectionOfTwoDateRange_thenResultIsCorrect() {
    final LocalDate date = LocalDate.of(2000, 1, 20);
    final LocalDate one_date_after = date.plus(1, ChronoUnit.DAYS);
    final LocalDate two_date_after = date.plus(2, ChronoUnit.DAYS);
    final LocalDate five_date_after = date.plus(5, ChronoUnit.DAYS);

    DateRange<LocalDate> range1 = new DateRange<>(date, one_date_after);
    DateRange<LocalDate> range2 = new DateRange<>(two_date_after, five_date_after);

    Optional<DateRange<LocalDate>> result;

    result = DatetimeUtil.intersect(range1, range2);

    assertTrue(result.isEmpty());

    range1 = new DateRange<>(date, two_date_after);
    range2 = new DateRange<>(one_date_after, five_date_after);

    result = DatetimeUtil.intersect(range1, range2);
    assertTrue(result.isPresent());
    assertEquals(one_date_after, result.get().getFrom());
    assertEquals(two_date_after, result.get().getTo());

    range1 = new DateRange<>(date, five_date_after);
    range2 = new DateRange<>(one_date_after, two_date_after);

    result = DatetimeUtil.intersect(range1, range2);
    assertTrue(result.isPresent());
    assertEquals(one_date_after, result.get().getFrom());
    assertEquals(two_date_after, result.get().getTo());
  }

  @Test
  void whenSetTimeToMidDay_thenResultIsCorrect() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime result = DatetimeUtil.atMidOfDate(now);
    assertEquals(now.getYear(), result.getYear());
    assertEquals(now.getMonth(), result.getMonth());
    assertEquals(now.getDayOfMonth(), result.getDayOfMonth());
    assertEquals(12, result.getHour());
    assertEquals(0, result.getMinute());
    assertEquals(0, result.getSecond());
  }
}