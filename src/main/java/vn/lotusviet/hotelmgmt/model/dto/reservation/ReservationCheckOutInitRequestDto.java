package vn.lotusviet.hotelmgmt.model.dto.reservation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Map;

public class ReservationCheckOutInitRequestDto {

  @NotNull private LocalDate checkInAt;
  @NotNull private LocalDate checkOutAt;

  @NotNull
  @Size(min = 1)
  private Map<Integer, Integer> suiteIdMapping;

  public LocalDate getCheckInAt() {
    return checkInAt;
  }

  public ReservationCheckOutInitRequestDto setCheckInAt(final LocalDate checkInAt) {
    this.checkInAt = checkInAt;
    return this;
  }

  public LocalDate getCheckOutAt() {
    return checkOutAt;
  }

  public ReservationCheckOutInitRequestDto setCheckOutAt(final LocalDate checkOutAt) {
    this.checkOutAt = checkOutAt;
    return this;
  }

  public Map<Integer, Integer> getSuiteIdMapping() {
    return suiteIdMapping;
  }

  public ReservationCheckOutInitRequestDto setSuiteIdMapping(
      final Map<Integer, Integer> suiteIdMapping) {
    this.suiteIdMapping = suiteIdMapping;
    return this;
  }

  @Override
  public String toString() {
    return "ReservationCheckOutInitRequestDto{"
        + "checkInAt="
        + checkInAt
        + ", checkOutAt="
        + checkOutAt
        + ", bookedSuiteIds="
        + suiteIdMapping
        + '}';
  }
}