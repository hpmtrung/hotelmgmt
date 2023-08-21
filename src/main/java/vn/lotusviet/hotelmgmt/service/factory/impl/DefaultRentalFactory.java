package vn.lotusviet.hotelmgmt.service.factory.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalPaymentContent;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalPaymentCreateDto;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalPayment;
import vn.lotusviet.hotelmgmt.model.entity.reservation.Reservation;
import vn.lotusviet.hotelmgmt.model.entity.room.Room;
import vn.lotusviet.hotelmgmt.service.CommonService;
import vn.lotusviet.hotelmgmt.service.EmployeeService;
import vn.lotusviet.hotelmgmt.service.PromotionService;
import vn.lotusviet.hotelmgmt.service.RoomService;
import vn.lotusviet.hotelmgmt.service.factory.RentalFactory;
import vn.lotusviet.hotelmgmt.util.DatetimeUtil;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatusCode.NO_CHECKOUT;

@Component
public final class DefaultRentalFactory implements RentalFactory {

  private final CommonService commonService;
  private final EmployeeService employeeService;
  private final RoomService roomService;
  private final PromotionService promotionService;

  public DefaultRentalFactory(
      CommonService commonService,
      EmployeeService employeeService,
      RoomService roomService,
      PromotionService promotionService) {
    this.commonService = commonService;
    this.employeeService = employeeService;
    this.roomService = roomService;
    this.promotionService = promotionService;
  }

  @Override
  public RentalDetail createRentalDetail(
      LocalDateTime checkInAt,
      LocalDateTime checkOutAt,
      int roomId,
      int suiteOriginalPrice,
      int vat,
      int discountAmount,
      int extraAmount,
      Set<Customer> customers) {
    Objects.requireNonNull(checkInAt);
    Objects.requireNonNull(checkOutAt);
    Objects.requireNonNull(customers);

    final Room room = roomService.getRoomById(roomId);

    final RentalDetail rentalDetail =
        new RentalDetail()
            .setCheckInAt(checkInAt)
            .setCheckOutAt(checkOutAt)
            .setRoom(room)
            .setRoomPrice(suiteOriginalPrice)
            .setVat(vat)
            .setDiscountAmount(discountAmount)
            .setExtraAmount(extraAmount)
            .setPromotions(
                promotionService.getSuitePromotionsFromDate(
                    room.getSuiteId(), checkInAt.toLocalDate(), checkOutAt.toLocalDate()))
            .setCustomers(customers);

    rentalDetail.updateSubTotal();
    return rentalDetail;
  }

  @Override
  public RentalPayment createRentalPayment(
      final LocalDateTime createdAt, final RentalPaymentCreateDto rentalPaymentCreateDto) {
    Objects.requireNonNull(createdAt);
    Objects.requireNonNull(rentalPaymentCreateDto);

    return new RentalPayment()
        .setCreatedAt(createdAt)
        .setMoney(rentalPaymentCreateDto.getMoney())
        .setContent(
            "Chi "
                + rentalPaymentCreateDto.getContents().stream()
                    .map(RentalPaymentContent::getValue)
                    .collect(Collectors.joining(", ")))
        .setCreatedBy(employeeService.getAuditedLogin());
  }

  @Override
  public Rental createRentalFromReservation(
      final @NotNull Reservation reservation, int newDiscountAmount) {
    Objects.requireNonNull(reservation);

    return new Rental()
        .setCreatedAt(LocalDateTime.now())
        .setCheckInAt(DatetimeUtil.atMidOfDate(reservation.getCheckInAt()))
        .setCheckOutAt(DatetimeUtil.atMidOfDate(reservation.getCheckOutAt()))
        .setDiscountAmount(newDiscountAmount)
        .setReservation(reservation)
        .setCreatedBy(employeeService.getAuditedLogin())
        .setOwner(reservation.getOwner())
        .setStatus(commonService.getRentalStatusByCode(NO_CHECKOUT));
  }

  @Override
  public Rental createRental(@NotNull final RentalCreateDto rentalCreateDto) {
    Objects.requireNonNull(rentalCreateDto);

    return new Rental()
        .setCreatedAt(LocalDateTime.now())
        .setCheckInAt(DatetimeUtil.atMidOfDate(rentalCreateDto.getCheckInAt()))
        .setCheckOutAt(DatetimeUtil.atMidOfDate(rentalCreateDto.getCheckOutAt()))
        .setDiscountAmount(rentalCreateDto.getRentalDiscountAmount())
        .setCreatedBy(employeeService.getAuditedLogin())
        .setStatus(commonService.getRentalStatusByCode(NO_CHECKOUT));
  }
}