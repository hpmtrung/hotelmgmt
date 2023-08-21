package vn.lotusviet.hotelmgmt.repository.room;

import vn.lotusviet.hotelmgmt.model.dto.room.RoomDto;
import vn.lotusviet.hotelmgmt.model.dto.room.RoomMapFilterOptionDTO;
import vn.lotusviet.hotelmgmt.model.dto.stats.RoomStatusStatsDto;
import vn.lotusviet.hotelmgmt.model.dto.stats.RoomStatusStatsRecord;

import java.util.List;

public interface RoomCustomRepository {

  RoomStatusStatsDto getRoomStatusStats();

  List<RoomStatusStatsRecord> getRoomStatusStatsRecords();

  List<RoomDto> getAllRoomsWithRentalDetail(RoomMapFilterOptionDTO filterDto);
}