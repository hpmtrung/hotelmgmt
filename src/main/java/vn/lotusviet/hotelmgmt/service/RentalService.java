package vn.lotusviet.hotelmgmt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceDto;
import vn.lotusviet.hotelmgmt.model.dto.rental.*;
import vn.lotusviet.hotelmgmt.model.dto.reservation.RentabilityRoomsMappingDto;
import vn.lotusviet.hotelmgmt.model.dto.room.RoomDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceUsageCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.service.ServiceUsagePayDto;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentalService {

  Rental getRentalById(long id);

  Page<RentalBasicDto> getActiveRentals(Pageable pageable);

  Page<RentalBasicDto> getRentalByFilterOption(
      RentalFilterOption rentalFilterOption, Pageable pageable);

  RentalDto getDetailOfRental(long id);

  RentalDetailDto getRentalDetailById(long detailId);

  RentalDto saveNewRental(RentalCreateDto rentalCreateDto);

  RentalDetail setRentalDetailPromotions(RentalDetail rentalDetail);

  RentalDetailDto payingServiceUsageDetail(
      long rentalDetailId, ServiceUsagePayDto serviceUsagePayDto);

  RentalDetailDto addServiceUsageDetails(
      long rentalDetailId, ServiceUsageCreateDto serviceUsageCreateDto);

  InvoiceDto payingRentalDetails(
      long rentalId, RentalDetailPayingRequestDto rentalDetailPayingRequestDto);

  InvoiceDto payingAllOfRental(long rentalId, RentalPayingRequestDto rentalPayingRequestDto);

  RentalPayingReviewResponseDto getRentalDetailPayingReview(
      long rentalId, List<Long> expectedPaidRentalDetailIds);

  RentalPayingReviewResponseDto getRentalDetailPayingReviewForRoomChangeDifferentSuite(long rentalDetailId);

  RentabilityRoomsMappingDto getRentabilityRoomMappingFromReservation(long reservationId);

  Optional<LocalDate> getRentalDetailMaximumCheckOutDate(long rentalDetailId);

  RentalDetailDto updateRentalDetail(
      long rentalDetailId, RentalDetailUpdateDto rentalDetailUpdateDto);

  List<RoomDto> getRentalDetailRoomChangeSuggestions(long rentalDetailId, boolean isSameSuite);

  RentalDetailDto changeRoomSameSuiteForRentalDetail(long rentalDetailId, int roomId);

  RentalDetailDto changeRoomDifferentSuiteForRentalDetail(long rentalDetailId, int roomId);

  boolean isCustomersRented(List<String> personalIds);
}