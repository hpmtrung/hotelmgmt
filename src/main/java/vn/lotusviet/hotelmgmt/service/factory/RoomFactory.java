package vn.lotusviet.hotelmgmt.service.factory;

import vn.lotusviet.hotelmgmt.model.dto.room.SuiteCreateDto;
import vn.lotusviet.hotelmgmt.model.entity.room.Room;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatusCode;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;
import vn.lotusviet.hotelmgmt.model.entity.tracking.SuitePriceHistory;

public interface RoomFactory {

  Suite createNewSuite(SuiteCreateDto suiteCreateDto);

  Room createNewRoom(String name, int floor, boolean available, RoomStatusCode roomStatusCode);

  SuitePriceHistory createSuitePriceHistory(Suite suite, int newPrice, int newVat);
}