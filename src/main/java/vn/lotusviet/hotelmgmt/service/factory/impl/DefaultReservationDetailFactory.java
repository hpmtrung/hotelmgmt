package vn.lotusviet.hotelmgmt.service.factory.impl;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationDetail;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;
import vn.lotusviet.hotelmgmt.service.RoomService;
import vn.lotusviet.hotelmgmt.service.factory.ReservationDetailFactory;

import java.util.List;

@Component
public final class DefaultReservationDetailFactory implements ReservationDetailFactory {

  private final RoomService roomService;

  public DefaultReservationDetailFactory(RoomService roomService) {
    this.roomService = roomService;
  }

  @Override
  public ReservationDetail create(int suiteId, int numRoom, @Nullable List<PromotionDto> appliedPromotions) {
    final Suite suite = roomService.getSuiteById(suiteId);
    return new ReservationDetail()
        .setSuite(suite)
        .setRoomNum(numRoom)
        .setSuitePrice(suite.getOriginalPrice())
        .setVat(suite.getVat())
        .setPromotions(appliedPromotions);
  }

}