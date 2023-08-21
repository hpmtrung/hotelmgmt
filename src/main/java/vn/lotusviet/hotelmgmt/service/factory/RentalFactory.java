package vn.lotusviet.hotelmgmt.service.factory;

import org.jetbrains.annotations.NotNull;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalPaymentCreateDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalPayment;
import vn.lotusviet.hotelmgmt.model.entity.reservation.Reservation;

import java.time.LocalDateTime;
import java.util.Set;

public interface RentalFactory {

  Rental createRentalFromReservation(@NotNull Reservation reservation, int newDiscountAmount);

  Rental createRental(@NotNull RentalCreateDto rentalCreateDto);

  RentalDetail createRentalDetail(
      LocalDateTime checkInAt,
      LocalDateTime checkOutAt,
      int roomId,
      int suiteOriginalPrice,
      int vat,
      int discountAmount,
      int extraAmount,
      Set<Customer> customers);

  RentalPayment createRentalPayment(LocalDateTime createdAt, RentalPaymentCreateDto rentalPaymentCreateDto);
}