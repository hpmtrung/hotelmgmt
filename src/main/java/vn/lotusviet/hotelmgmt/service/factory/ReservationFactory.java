package vn.lotusviet.hotelmgmt.service.factory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationCreateDto;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.model.entity.reservation.Reservation;

import java.time.LocalDate;

public interface ReservationFactory {

  Reservation createTemporaryReservation(
      @NotNull LocalDate checkInAt,
      @NotNull LocalDate checkOutAt,
      @NotNull PaymentMethodCode paymentMethodCode,
      @Nullable Customer owner);

  Reservation createReservation(
      @NotNull ReservationCreateDto reservationCreateDto, @Nullable Customer owner);
}