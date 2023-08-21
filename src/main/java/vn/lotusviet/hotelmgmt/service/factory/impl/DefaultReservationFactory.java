package vn.lotusviet.hotelmgmt.service.factory.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationCreateDto;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethod;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.model.entity.reservation.Reservation;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatus;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatusCode;
import vn.lotusviet.hotelmgmt.service.CommonService;
import vn.lotusviet.hotelmgmt.service.SystemService;
import vn.lotusviet.hotelmgmt.service.factory.ReservationFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

import static vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleKey.RESERVATION_TEMPORARY_DEPOSIT_MIN_PERCENT;
import static vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleKey.RESERVATION_TEMPORARY_TIMEOUT_MINUTES;
import static vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatusCode.COMPLETE_DEPOSIT;
import static vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatusCode.TEMPORARY;

@Component
public final class DefaultReservationFactory implements ReservationFactory {

  private final SystemService systemService;
  private final CommonService commonService;

  public DefaultReservationFactory(SystemService systemService, CommonService commonService) {
    this.systemService = systemService;
    this.commonService = commonService;
  }

  public Reservation createTemporaryReservation(
      @NotNull LocalDate checkInAt,
      @NotNull LocalDate checkOutAt,
      @NotNull PaymentMethodCode paymentMethodCode,
      @Nullable Customer owner) {
    return new Reservation()
        .setCreatedAt(Instant.now())
        .setCheckInAt(Objects.requireNonNull(checkInAt))
        .setCheckOutAt(Objects.requireNonNull(checkOutAt))
        .setDepositPercent(getCurrentDepositPercent())
        .setTimeElapsedMins(getCurrentTemporaryTimeElapsedMinutes())
        .setStatus(getReservationStatusByCode(TEMPORARY))
        .setPaymentMethod(getPaymentMethodByCode(paymentMethodCode))
        .setOwner(owner);
  }

  @Override
  public Reservation createReservation(
      @NotNull ReservationCreateDto reservationCreateDto, @Nullable Customer owner) {
    Objects.requireNonNull(reservationCreateDto);
    Objects.requireNonNull(owner);

    return new Reservation()
        .setCreatedAt(Instant.now())
        .setCheckInAt(reservationCreateDto.getCheckInAt())
        .setCheckOutAt(reservationCreateDto.getCheckOutAt())
        .setDepositAmount(reservationCreateDto.getDepositAmount())
        .setSpecialRequirements(reservationCreateDto.getSpecialRequirements().trim())
        .setDepositPercent(getCurrentDepositPercent())
        .setTimeElapsedMins(getCurrentTemporaryTimeElapsedMinutes())
        .setStatus(getReservationStatusByCode(COMPLETE_DEPOSIT))
        .setPaymentMethod(getPaymentMethodByCode(reservationCreateDto.getPaymentMethodCode()))
        .setOwner(owner);
  }

  private ReservationStatus getReservationStatusByCode(ReservationStatusCode statusCode) {
    return commonService.getReservationStatusByCode(Objects.requireNonNull(statusCode));
  }

  private PaymentMethod getPaymentMethodByCode(PaymentMethodCode paymentMethodCode) {
    return commonService.getPaymentMethodByCode(Objects.requireNonNull(paymentMethodCode));
  }

  private int getCurrentTemporaryTimeElapsedMinutes() {
    return systemService.getBusinessRuleValue(RESERVATION_TEMPORARY_TIMEOUT_MINUTES, Integer.class);
  }

  private int getCurrentDepositPercent() {
    return systemService.getBusinessRuleValue(
        RESERVATION_TEMPORARY_DEPOSIT_MIN_PERCENT, Integer.class);
  }
}