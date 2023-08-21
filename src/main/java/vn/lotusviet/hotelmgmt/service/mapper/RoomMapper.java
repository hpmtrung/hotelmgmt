package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.*;
import vn.lotusviet.hotelmgmt.model.dto.room.RoomDto;
import vn.lotusviet.hotelmgmt.model.dto.room.RoomUpsertDto;
import vn.lotusviet.hotelmgmt.model.entity.room.Room;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {SuiteMapper.class})
public interface RoomMapper {

  String TO_ROOM_DTO_QUALIFIER = "toRoomDto";
  String TO_ROOM_DTO_SUITE_QUALIFIER = "toRoomDto_suite";
  String TO_ROOM_BASIC_DTO_QUALIFIER = "toRoomBasicDto";
  String TO_ROOM_BASIC_DTO_WITHOUT_SUITE_QUALIFIER = "toRoomBasicDtoWithoutSuite";

  @Named(TO_ROOM_DTO_QUALIFIER)
  @Mapping(target = "statusCode", source = "status.code")
  @Mapping(target = "rentalDetail", ignore = true)
  @Mapping(
      target = "suite",
      source = "suite",
      qualifiedByName = SuiteMapper.TO_SUITE_DTO_WITH_TYPE_STYLE_PRICE_QUALIFIER)
  RoomDto toRoomDto(Room entity);

  @IterableMapping(qualifiedByName = TO_ROOM_DTO_QUALIFIER)
  List<RoomDto> toRoomDto(List<Room> entity);

  @Named(TO_ROOM_BASIC_DTO_WITHOUT_SUITE_QUALIFIER)
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id")
  @Mapping(target = "name")
  @Mapping(target = "floor")
  @Mapping(target = "available")
  RoomDto toRoomBasicDtoWithoutSuite(Room entity);

  @IterableMapping(qualifiedByName = TO_ROOM_BASIC_DTO_WITHOUT_SUITE_QUALIFIER)
  List<RoomDto> toRoomBasicDtoWithoutSuite(List<Room> entity);

  @Named(TO_ROOM_BASIC_DTO_QUALIFIER)
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id")
  @Mapping(target = "name")
  @Mapping(target = "floor")
  @Mapping(target = "available")
  @Mapping(
      target = "suite",
      source = "suite",
      qualifiedByName = SuiteMapper.TO_SUITE_DTO_WITH_TYPE_STYLE_PRICE_QUALIFIER)
  RoomDto toRoomBasicDto(Room entity);

  @IterableMapping(qualifiedByName = TO_ROOM_BASIC_DTO_QUALIFIER)
  List<RoomDto> toRoomBasicDto(List<Room> entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "suite", ignore = true)
  @Mapping(target = "suiteId", ignore = true)
  @Mapping(target = "status", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void partialUpdate(@MappingTarget Room entity, RoomUpsertDto dto);
}