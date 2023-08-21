package vn.lotusviet.hotelmgmt.repository.checkin;

import vn.lotusviet.hotelmgmt.model.dto.room.RoomDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentalDetailCustomRepository {

  void updateFeeOfOverduedRentalDetails(int feePercent);

  Optional<LocalDate> getMaximumCheckOutDate(long rentalDetailId, int dayThreshold);

  List<RoomDto> getRentalDetailRoomChangeSuggestions(long rentalDetailId, boolean isSameSuite);
}