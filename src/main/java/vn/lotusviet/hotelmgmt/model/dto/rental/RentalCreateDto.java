package vn.lotusviet.hotelmgmt.model.dto.rental;

import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationSaveToRentalRequestDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RentalCreateDto extends ReservationSaveToRentalRequestDto {

  @NotNull private String mainCustomerPersonalId;
  @NotNull private LocalDateTime checkInAt;
  @NotNull private LocalDateTime checkOutAt;

  public String getMainCustomerPersonalId() {
    return mainCustomerPersonalId;
  }

  public RentalCreateDto setMainCustomerPersonalId(String mainCustomerPersonalId) {
    this.mainCustomerPersonalId = mainCustomerPersonalId;
    return this;
  }

  public LocalDateTime getCheckInAt() {
    return checkInAt;
  }

  public RentalCreateDto setCheckInAt(final LocalDateTime checkInAt) {
    this.checkInAt = checkInAt;
    return this;
  }

  public LocalDateTime getCheckOutAt() {
    return checkOutAt;
  }

  public RentalCreateDto setCheckOutAt(final LocalDateTime checkOutAt) {
    this.checkOutAt = checkOutAt;
    return this;
  }

  @Override
  public String toString() {
    return "RentalCreateDto{"
        + "checkInAt="
        + checkInAt
        + ", checkOutAt="
        + checkOutAt
        + "} "
        + super.toString();
  }
}