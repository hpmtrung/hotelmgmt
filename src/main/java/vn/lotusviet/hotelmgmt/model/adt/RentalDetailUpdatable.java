package vn.lotusviet.hotelmgmt.model.adt;

import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalDetailUpdatable {

  RentalDetailUpdatable setPromotions(List<PromotionDto> promotions);

  int getSuiteId();

  LocalDateTime getCheckInAt();

  RentalDetailUpdatable setCheckOutAt(LocalDateTime checkOutAt);

  LocalDateTime getCheckOutAt();

  void updateSubTotal();

  int getSubTotal();
}