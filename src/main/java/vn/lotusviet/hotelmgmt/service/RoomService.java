package vn.lotusviet.hotelmgmt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.lotusviet.hotelmgmt.model.dto.room.*;
import vn.lotusviet.hotelmgmt.model.dto.stats.RoomStatusStatsDto;
import vn.lotusviet.hotelmgmt.model.dto.tracking.SuitePriceHistoryDto;
import vn.lotusviet.hotelmgmt.model.entity.room.Room;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatusCode;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;

import java.util.List;

public interface RoomService {

  // ========================================================================
  // SUITE
  // ========================================================================

  Page<SuiteDto> getSuiteSearchResults(
      SuiteSearchRequestDto suiteSearchRequestDto, Pageable pageable);

  boolean isSuiteIdExist(int suiteId);

  Suite getSuiteById(int suiteId);

  Page<SuitePriceHistoryDto> getSuitePriceHistory(int suiteId, Pageable pageable);

  SuiteStyleDto saveNewSuiteStyle(SuiteStyleUpsertDto suiteStyleUpsertDto);

  SuiteStyleDto updateSuiteStyle(int suiteStyleId, SuiteStyleUpsertDto suiteStyleUpsertDto);

  SuiteTypeDto saveNewSuiteType(SuiteTypeUpsertDto suiteTypeUpsertDto);

  SuiteTypeDto updateSuiteType(int suiteTypeId, SuiteStyleUpsertDto suiteStyleUpsertDto);

  SuiteDto saveNewSuite(SuiteCreateDto suiteCreateDto, MultipartFile imageFile);

  SuiteDto updateSuite(int suiteId, SuiteUpdateDto dto);

  SuiteDto updateSuiteAvailability(int suiteId, boolean available);

  SuiteDto updateSuiteImage(int suiteId, MultipartFile file);

  SuiteDto addNewRoomsForSuite(int suiteId, List<RoomUpsertDto> rooms);

  List<SuiteDto> getBestSaleSuites(Integer nSuite);

  List<SuiteDto> getAllSuites();

  SuiteDto getDetailOfSuite(int suiteId);

  // ========================================================================
  // ROOM
  // ========================================================================

  List<Integer> getFoorNumbers();

  boolean isRoomExistById(int roomId);

  boolean isRoomNameDuplicated(String name);

  Room getRoomById(int roomId);

  List<RoomDto> getAllRoomsWithBasicInfo(RoomMapFilterOptionDTO roomMapFilterOptionDTO);

  RoomDto updateStatusOfRoom(int roomId, RoomStatusCode roomStatusCode);

  RoomStatusStatsDto getRoomStatusStatistics();

  RoomDto getDetailOfRoomById(int roomId);

  RoomDto updateRoom(int roomId, RoomUpsertDto roomUpsertDto);

  RoomDto updateRoomAvailability(int roomId, boolean available);

  boolean isRoomBelongToSuite(int roomId, int suiteId);

  void clearSuiteCache(Suite suite);
}