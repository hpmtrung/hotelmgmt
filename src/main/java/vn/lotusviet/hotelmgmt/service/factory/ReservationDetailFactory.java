package vn.lotusviet.hotelmgmt.service.factory;

import org.jetbrains.annotations.Nullable;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationDetail;

import java.util.List;

public interface ReservationDetailFactory {

  ReservationDetail create(
      int suiteId,
      int numRoom,
      @Nullable List<PromotionDto> appliedPromotions);
}