package vn.lotusviet.hotelmgmt.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.util.Objects;
import java.util.Optional;

public final class DatetimeUtil {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  private DatetimeUtil() {}

  public static String formatLocalDate(final LocalDate date) {
    Objects.requireNonNull(date);
    return DATE_FORMATTER.format(date);
  }

  public static String formatLocalDateTime(final LocalDateTime dateTime) {
    Objects.requireNonNull(dateTime);
    return DATE_TIME_FORMATTER.format(dateTime);
  }

  public static LocalDateTime atStartOfDate(LocalDate date) {
    return Objects.requireNonNull(date).atStartOfDay();
  }

  public static LocalDateTime atEndOfDate(LocalDate date) {
    return LocalTime.MAX.atDate(Objects.requireNonNull(date));
  }

  public static LocalDate atStartOfMonth(LocalDate value) {
    Objects.requireNonNull(value);
    return value.with(ChronoField.DAY_OF_MONTH, 1);
  }

  public static LocalDate atEndOfMonth(LocalDate value) {
    Objects.requireNonNull(value);
    return value.withDayOfMonth(value.getMonth().length(value.isLeapYear()));
  }

  public static LocalDateTime atMidOfDate(LocalDateTime value) {
    value = LocalDateTime.of(value.getYear(), value.getMonth(), value.getDayOfMonth(), 12, 0, 0);
    return value;
  }

  public static LocalDateTime atMidOfDate(LocalDate value) {
    return LocalDateTime.of(value.getYear(), value.getMonth(), value.getDayOfMonth(), 12, 0, 0);
  }

  public static Optional<DateRange<LocalDate>> intersect(
      final DateRange<LocalDate> range1, final DateRange<LocalDate> range2) {

    LocalDate from1 = range1.from;
    LocalDate to1 = range1.to;
    LocalDate from2 = range2.from;
    LocalDate to2 = range2.to;

    if (from1.isAfter(to1))
      throw new IllegalArgumentException(
          String.format("Illegal date [from: '%s', to: '%s']", from1, to1));
    if (from2.isAfter(to2))
      throw new IllegalArgumentException(
          String.format("Illegal date [from: '%s', to: '%s']", from2, to2));

    if (!from1.isAfter(from2)) {
      if (to1.isBefore(from2)) return Optional.empty();
      if (!to1.isAfter(to2)) return Optional.of(new DateRange<>(from2, to1));
      return Optional.of(range2);
    } else if (!from1.isAfter(to2)) {
      if (!to1.isAfter(to2)) return Optional.of(range1);
      return Optional.of(new DateRange<>(from1, to2));
    }
    return Optional.empty();
  }

  public static class DateRange<T extends Temporal> {
    private T from;
    private T to;

    public DateRange(T from, T to) {
      this.from = Objects.requireNonNull(from);
      this.to = Objects.requireNonNull(to);
    }

    public static <D extends Temporal> DateRange<D> of(D dateFrom, D dateTo) {
      Objects.requireNonNull(dateFrom);
      Objects.requireNonNull(dateTo);
      return new DateRange<>(dateFrom, dateTo);
    }

    public T getFrom() {
      return from;
    }

    public DateRange<T> setFrom(final T from) {
      this.from = Objects.requireNonNull(from);
      return this;
    }

    public T getTo() {
      return to;
    }

    public DateRange<T> setTo(final T to) {
      this.to = Objects.requireNonNull(to);
      return this;
    }

    @Override
    public String toString() {
      return "{" + "from=" + from + ", to=" + to + '}';
    }
  }
}