package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.*;
import vn.lotusviet.hotelmgmt.model.dto.room.*;
import vn.lotusviet.hotelmgmt.model.dto.tracking.SuitePriceHistoryDto;
import vn.lotusviet.hotelmgmt.model.entity.room.Room;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteStyle;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteType;
import vn.lotusviet.hotelmgmt.model.entity.tracking.SuitePriceHistory;
import vn.lotusviet.hotelmgmt.service.mapper.decorator.SuiteMapperDecorator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
    componentModel = "spring",
    uses = {EmployeeMapper.class, RoomMapper.class})
@DecoratedWith(SuiteMapperDecorator.class)
public interface SuiteMapper {

  String TO_SUITE_DTO_WITH_TYPE_STYLE_PRICE_QUALIFIER = "toSuiteDtoWithTypeAndStyleAndPriceInfo";
  String TO_BASIC_DTO_QUALIFIER = "toBasicDto";
  String TO_DTO_FOR_RESERVATION_QUALIFIER = "toDtoForReservation";

  @Named(TO_DTO_FOR_RESERVATION_QUALIFIER)
  @Mapping(target = "typeName", source = "suiteType.name")
  @Mapping(target = "styleName", source = "suiteStyle.name")
  @Mapping(target = "maxOccupation", source = "suiteStyle.maxOccupation")
  @Mapping(target = "rooms", ignore = true)
  @Mapping(target = "emptyRoomNum", ignore = true)
  @Mapping(target = "promotion", ignore = true)
  @Mapping(target = "amenityIds", ignore = true)
  SuiteDto toDtoForReservation(Suite entity);

  @Named(TO_BASIC_DTO_QUALIFIER)
  @Mapping(target = "typeName", source = "suiteType.name")
  @Mapping(target = "styleName", source = "suiteStyle.name")
  @Mapping(target = "maxOccupation", source = "suiteStyle.maxOccupation")
  @Mapping(target = "rooms", ignore = true)
  @Mapping(target = "promotion", ignore = true)
  @Mapping(target = "emptyRoomNum", ignore = true)
  @Mapping(target = "amenityIds", ignore = true)
  SuiteDto toBasicDto(Suite entity);

  @Mapping(target = "typeName", source = "suiteType.name")
  @Mapping(target = "styleName", source = "suiteStyle.name")
  @Mapping(target = "maxOccupation", source = "suiteStyle.maxOccupation")
  @Mapping(target = "rooms", qualifiedByName = "getBasicRoomsOfSuite")
  @Mapping(target = "promotion", ignore = true)
  @Mapping(target = "emptyRoomNum", ignore = true)
  @Mapping(target = "amenityIds", ignore = true)
  SuiteDto toDto(Suite entity);

  @Named(TO_SUITE_DTO_WITH_TYPE_STYLE_PRICE_QUALIFIER)
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  @Mapping(target = "typeName", source = "suiteType.name")
  @Mapping(target = "styleName", source = "suiteStyle.name")
  @Mapping(target = "originalPrice", source = "originalPrice")
  @Mapping(target = "available")
  SuiteDto toSuiteDtoWithTypeAndStyleAndPriceInfo(Suite entity);

  @Mapping(target = "suite", ignore = true)
  SuitePriceHistoryDto toPriceHistoryDto(SuitePriceHistory entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "suites", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void partialUpdateSuiteStyle(SuiteStyleUpsertDto dto, @MappingTarget SuiteStyle entity);

  SuiteStyleDto toSuiteStyleDto(SuiteStyle entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void partialUpdateSuiteType(SuiteStyleUpsertDto dto, @MappingTarget SuiteType entity);

  SuiteTypeDto toSuiteTypeDto(SuiteType entity);

  @Mapping(target = "originalPrice", source = "price")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void partialUpdateSuite(SuiteUpdateDto suiteUpdateDto, @MappingTarget Suite entity);

  @Named("getBasicRoomsOfSuite")
  default List<RoomDto> getBasicRoomOfSuite(Set<Room> rooms) {
    return rooms.stream()
        .map(
            room ->
                new RoomDto()
                    .setId(room.getId())
                    .setName(room.getName())
                    .setFloor(room.getFloor())
                    .setAvailable(room.isAvailable()))
        .collect(Collectors.toList());
  }
}