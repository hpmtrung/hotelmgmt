package vn.lotusviet.hotelmgmt.service.factory.impl;

import org.springframework.stereotype.Component;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteCreateDto;
import vn.lotusviet.hotelmgmt.model.entity.room.Room;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatusCode;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;
import vn.lotusviet.hotelmgmt.model.entity.tracking.SuitePriceHistory;
import vn.lotusviet.hotelmgmt.service.CommonService;
import vn.lotusviet.hotelmgmt.service.EmployeeService;
import vn.lotusviet.hotelmgmt.service.factory.RoomFactory;

import java.time.LocalDate;
import java.util.Objects;

@Component
public final class DefaultRoomFactory implements RoomFactory {

  private final EmployeeService employeeService;
  private final CommonService commonService;

  public DefaultRoomFactory(EmployeeService employeeService, CommonService commonService) {
    this.employeeService = employeeService;
    this.commonService = commonService;
  }

  @Override
  public Suite createNewSuite(final SuiteCreateDto suiteCreateDto) {
    Objects.requireNonNull(suiteCreateDto);

    return new Suite()
        .setSuiteType(commonService.getSuiteTypeById(suiteCreateDto.getSuiteTypeId()))
        .setSuiteStyle(commonService.getSuiteStyleById(suiteCreateDto.getSuiteStyleId()))
        .setOriginalPrice(suiteCreateDto.getPrice())
        .setVat(suiteCreateDto.getVat())
        .setArea(suiteCreateDto.getArea());
  }

  @Override
  public Room createNewRoom(
      String name, int floor, boolean available, RoomStatusCode roomStatusCode) {
    Objects.requireNonNull(name);

    return new Room()
        .setName(name)
        .setFloor(floor)
        .setStatus(commonService.getRoomStatusByCode(roomStatusCode))
        .setAvailable(available);
  }

  @Override
  public SuitePriceHistory createSuitePriceHistory(final Suite suite, int newPrice, int newVat) {
    Objects.requireNonNull(suite);

    return new SuitePriceHistory()
        .setEditedAt(LocalDate.now())
        .setEditedBy(employeeService.getAuditedLogin())
        .setPrice(newPrice)
        .setVat(newVat)
        .setSuite(suite);
  }
}