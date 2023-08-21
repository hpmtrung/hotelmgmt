package vn.lotusviet.hotelmgmt.web.rest;

import com.vladmihalcea.concurrent.Retry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.annotation.security.AdminSecured;
import vn.lotusviet.hotelmgmt.core.annotation.security.PortalSecured;
import vn.lotusviet.hotelmgmt.core.annotation.validation.image.ValidImage;
import vn.lotusviet.hotelmgmt.core.exception.InvalidParamException;
import vn.lotusviet.hotelmgmt.model.dto.room.*;
import vn.lotusviet.hotelmgmt.model.dto.stats.RoomStatusStatsDto;
import vn.lotusviet.hotelmgmt.model.dto.tracking.SuitePriceHistoryDto;
import vn.lotusviet.hotelmgmt.model.entity.room.RoomStatusCode;
import vn.lotusviet.hotelmgmt.service.RoomService;
import vn.lotusviet.hotelmgmt.util.PaginationUtil;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Map;

import static vn.lotusviet.hotelmgmt.exception.ApplicationErrorCode.ROOM_ID_NOT_EXIST;
import static vn.lotusviet.hotelmgmt.web.rest.RoomController.URL_PREFIX;

@RestController
@Validated
@RequestMapping(URL_PREFIX)
public class RoomController {

  public static final String URL_PREFIX = "api/v1/room";

  private final RoomService roomService;

  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @PortalSecured
  @GetMapping("/map")
  public RoomFloorMappingDto getRoomMap(final RoomMapFilterOptionDTO roomMapFilterOptionDTO) {
    return new RoomFloorMappingDto(roomService.getAllRoomsWithBasicInfo(roomMapFilterOptionDTO));
  }

  @PortalSecured
  @GetMapping("/floor_numbers")
  public ResponseEntity<Object> getRoomFloorNumbers() {
    List<Integer> floorNumbers = roomService.getFoorNumbers();
    return ResponseEntity.ok(Map.of("floorNumbers", floorNumbers));
  }

  @PortalSecured
  @GetMapping("/detail/{roomId}")
  public RoomDto getDetailOfRoom(final @PathVariable @PositiveOrZero int roomId) {
    verifyRoomIdExist(roomId);
    return roomService.getDetailOfRoomById(roomId);
  }

  @PortalSecured
  @LogAround(output = false)
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/detail/{roomId}/update")
  public RoomDto updateRoomInfo(
      final @PathVariable @PositiveOrZero int roomId,
      final @Valid @RequestBody RoomUpsertDto roomUpsertDto) {
    return roomService.updateRoom(roomId, roomUpsertDto);
  }

  @AdminSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/detail/{roomId}/update_availability")
  public RoomDto updateRoomAvailability(
      final @PathVariable int roomId, final @RequestParam boolean available) {
    return roomService.updateRoomAvailability(roomId, available);
  }

  @PortalSecured
  @GetMapping("/status/statistics")
  public RoomStatusStatsDto getRoomStatusStatistics() {
    return roomService.getRoomStatusStatistics();
  }

  @PortalSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/status/{roomId}/update_status")
  public RoomDto updateRoomStatus(
      final @PathVariable @PositiveOrZero int roomId, final @RequestParam RoomStatusCode status) {
    verifyRoomIdExist(roomId);
    return roomService.updateStatusOfRoom(roomId, status);
  }

  @LogAround(output = false)
  @GetMapping("/suite/bestsale")
  public List<SuiteDto> getBestSaleSuites(final @RequestParam(required = false) Integer nSuite) {
    return roomService.getBestSaleSuites(nSuite);
  }

  @GetMapping("/suite/search")
  public ResponseEntity<List<SuiteDto>> getSuitesFromSearch(
      final @Valid SuiteSearchRequestDto suiteSearchRequestDto, final Pageable pageable) {
    return PaginationUtil.createPaginationResponse(
        roomService.getSuiteSearchResults(suiteSearchRequestDto, pageable));
  }

  @AdminSecured
  @GetMapping("/suite")
  public List<SuiteDto> getAllSuites() {
    return roomService.getAllSuites();
  }

  @AdminSecured
  @GetMapping("/suite/{suiteId}")
  public SuiteDto getSuiteDetail(final @PathVariable int suiteId) {
    return roomService.getDetailOfSuite(suiteId);
  }

  @AdminSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/suite/{suiteId}/update_image")
  public SuiteDto updateSuiteImage(
      final @PathVariable int suiteId, final @ValidImage @RequestParam MultipartFile image) {
    return roomService.updateSuiteImage(suiteId, image);
  }

  @AdminSecured
  @GetMapping("/suite/{suiteId}/price_history")
  public ResponseEntity<List<SuitePriceHistoryDto>> getSuitePriceHistory(
      final @PathVariable int suiteId, final Pageable pageable) {
    final Page<SuitePriceHistoryDto> page = roomService.getSuitePriceHistory(suiteId, pageable);
    return PaginationUtil.createPaginationResponse(page);
  }

  @AdminSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/suite/{suiteId}/update")
  public SuiteDto updateSuite(
      final @PathVariable int suiteId, final @Valid @RequestBody SuiteUpdateDto suiteUpdateDto) {
    return roomService.updateSuite(suiteId, suiteUpdateDto);
  }

  @AdminSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/suite/{suiteId}/update_availability")
  public SuiteDto updateSuiteAvailability(
      final @PathVariable int suiteId, final @RequestParam boolean available) {
    return roomService.updateSuiteAvailability(suiteId, available);
  }

  @AdminSecured
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/suite/{suiteId}/rooms/add")
  public SuiteDto saveNewSuiteRooms(
      final @PathVariable int suiteId, final @Valid @RequestBody SuiteRoomAddDto suiteRoomAddDto) {
    return roomService.addNewRoomsForSuite(suiteId, suiteRoomAddDto.getRooms());
  }

  @AdminSecured
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/suite/create")
  public SuiteDto saveNewSuite(
      final @RequestPart("data") SuiteCreateDto suiteCreateDto,
      final @RequestPart("image") MultipartFile imageFile) {
    return roomService.saveNewSuite(suiteCreateDto, imageFile);
  }

  @AdminSecured
  @ResponseStatus(code = HttpStatus.CREATED)
  @PostMapping("/suite_style/create")
  public SuiteStyleDto saveNewSuiteStyle(
      final @Valid @RequestBody SuiteStyleUpsertDto suiteStyleUpsertDto) {
    return roomService.saveNewSuiteStyle(suiteStyleUpsertDto);
  }

  @AdminSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/suite_style/{suiteStyleId}/update")
  public SuiteStyleDto updateSuiteStyle(
      final @PathVariable int suiteStyleId,
      final @Valid @RequestBody SuiteStyleUpsertDto suiteStyleUpsertDto) {
    return roomService.updateSuiteStyle(suiteStyleId, suiteStyleUpsertDto);
  }

  @AdminSecured
  @ResponseStatus(code = HttpStatus.CREATED)
  @PostMapping("/suite_type/create")
  public SuiteTypeDto saveNewSuiteType(
      final @Valid @RequestBody SuiteTypeUpsertDto suiteTypeUpsertDto) {
    return roomService.saveNewSuiteType(suiteTypeUpsertDto);
  }

  @AdminSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/suite_type/{suiteTypeId}/update/")
  public SuiteTypeDto updateSuiteType(
      final @PathVariable Integer suiteTypeId,
      final @Valid @RequestBody SuiteStyleUpsertDto suiteStyleUpsertDto) {
    return roomService.updateSuiteType(suiteTypeId, suiteStyleUpsertDto);
  }

  private void verifyRoomIdExist(final int roomId) {
    boolean isRoomNotExist = !roomService.isRoomExistById(roomId);
    if (isRoomNotExist) {
      throw InvalidParamException.builder()
          .path(ROOM_ID_NOT_EXIST, "roomId", String.valueOf(roomId), "Room id is not found.")
          .build();
    }
  }
}