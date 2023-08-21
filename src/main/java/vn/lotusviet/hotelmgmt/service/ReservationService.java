package vn.lotusviet.hotelmgmt.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalDto;
import vn.lotusviet.hotelmgmt.model.dto.reservation.*;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteReservationDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteSearchRequestDto;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethodCode;
import vn.lotusviet.hotelmgmt.model.entity.reservation.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

public interface ReservationService {

  Reservation getReservationById(long reservationId);

  Reservation getReservationByIdWithPromotionInfo(long reservationId);

  List<SuiteReservationDto> getAvailableSuitesByDate(
      LocalDate checkInAt, LocalDate checkOutAt, SuiteSearchRequestDto filters, Sort sort);

  ReservationSearchResponseDto getReservableSuites(
      LocalDate checkInAt, LocalDate checkOutAt, List<Integer> occupations);

  Page<ReservationDto> getReservationsOfCustomer(
      String personalId, Pageable pageable, Function<Reservation, ReservationDto> mappingFunction);

  ReservationDto saveNewTemporaryReservation(ReservationCheckOutInitRequestDto dto);

  ReservationDto getValidTemporaryReservationById(long id);

  ReservationDto saveReservationOwnerInfo(long id);

  ReservationDto saveNewReservationFromTemporary(
      long temporaryReservationId, PaymentMethodCode paymentMethod);

  ReservationDto saveTemporaryReservation(ReservationCheckOutFinishRequestDto requestDto);

  Page<ReservationDto> getReservationsFromDateRange(
      LocalDate checkInAt,
      LocalDate checkOutAt,
      String statuses,
      String personalId,
      Pageable pageable);

  ReservationDto getDetailOfReservation(
      long reservaionId, @NotNull Function<Reservation, ReservationDto> mapperFunction);

  ReservationDto getReservationWithoutPromotionInfo(long id);

  RentalDto saveNewRentalFromReservation(long id, ReservationSaveToRentalRequestDto dto);

  ReservationDto saveReservation(ReservationCreateDto dto);

  ReservationDto updateReservation(long id, ReservationUpdateDto dto);

  ReservationDto cancelReservation(long id, ReservationCancelRequestDto cancelRequestDto);

  boolean isReservationExistByIdAndOwnerId(long reservationId, @NotNull String reservationOwnerId);
}