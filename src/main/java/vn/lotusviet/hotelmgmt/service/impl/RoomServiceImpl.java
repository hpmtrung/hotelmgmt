package vn.lotusviet.hotelmgmt.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.exception.ConstraintViolationException;
import vn.lotusviet.hotelmgmt.core.exception.InvalidParamException;
import vn.lotusviet.hotelmgmt.exception.*;
import vn.lotusviet.hotelmgmt.model.dto.room.*;
import vn.lotusviet.hotelmgmt.model.dto.stats.RoomStatusStatsDto;
import vn.lotusviet.hotelmgmt.model.dto.tracking.SuitePriceHistoryDto;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;
import vn.lotusviet.hotelmgmt.model.entity.room.*;
import vn.lotusviet.hotelmgmt.model.entity.tracking.SuitePriceHistory;
import vn.lotusviet.hotelmgmt.model.entity.tracking.SuitePriceHistory.SuitePriceHistoryId;
import vn.lotusviet.hotelmgmt.repository.checkin.RentalDetailRepository;
import vn.lotusviet.hotelmgmt.repository.room.*;
import vn.lotusviet.hotelmgmt.repository.tracking.SuitePriceHistoryRepository;
import vn.lotusviet.hotelmgmt.service.CommonService;
import vn.lotusviet.hotelmgmt.service.PromotionService;
import vn.lotusviet.hotelmgmt.service.RoomService;
import vn.lotusviet.hotelmgmt.service.factory.RoomFactory;
import vn.lotusviet.hotelmgmt.service.mapper.RentalMapper;
import vn.lotusviet.hotelmgmt.service.mapper.RoomMapper;
import vn.lotusviet.hotelmgmt.service.mapper.SuiteMapper;
import vn.lotusviet.hotelmgmt.service.storage.StorageService;
import vn.lotusviet.hotelmgmt.util.FileUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static vn.lotusviet.hotelmgmt.exception.ApplicationErrorCode.ROOM_NAME_DUPLICATED;
import static vn.lotusviet.hotelmgmt.service.storage.StorageService.FileBrand.UPLOAD_IMG;
import static vn.lotusviet.hotelmgmt.service.storage.StorageService.FileCategory.SUITE;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {

  public static final String CACHE_ROOM_FLOOR_NUMBERS = "CacheRoomFloorNumbers";
  private static final Logger log = LoggerFactory.getLogger(RoomServiceImpl.class);
  private static final int DEFAULT_BEST_SALE_SUITES_NUM = 10;
  private final RoomRepository roomRepository;
  private final RoomMapper roomMapper;
  private final SuiteRepository suiteRepository;
  private final SuitePriceHistoryRepository suitePriceHistoryRepository;
  private final SuiteMapper suiteMapper;
  private final SuiteStyleRepository suiteStyleRepository;
  private final SuiteTypeRepository suiteTypeRepository;
  private final StorageService storageService;
  private final PromotionService promotionService;
  private final RentalMapper rentalMapper;
  private final RentalDetailRepository rentalDetailRepository;
  private final RoomFactory roomFactory;
  private final CommonService commonService;
  private final AmenityRepository amenityRepository;
  private final CacheManager cacheManager;

  public RoomServiceImpl(
      RoomRepository roomRepository,
      RoomMapper roomMapper,
      SuiteRepository suiteRepository,
      SuitePriceHistoryRepository suitePriceHistoryRepository,
      SuiteMapper suiteMapper,
      SuiteStyleRepository suiteStyleRepository,
      SuiteTypeRepository suiteTypeRepository,
      StorageService storageService,
      PromotionService promotionService,
      RentalMapper rentalMapper,
      RentalDetailRepository rentalDetailRepository,
      RoomFactory roomFactory,
      CommonService commonService,
      AmenityRepository amenityRepository,
      CacheManager cacheManager) {
    this.roomRepository = roomRepository;
    this.roomMapper = roomMapper;
    this.suiteRepository = suiteRepository;
    this.suitePriceHistoryRepository = suitePriceHistoryRepository;
    this.suiteMapper = suiteMapper;
    this.suiteStyleRepository = suiteStyleRepository;
    this.suiteTypeRepository = suiteTypeRepository;
    this.storageService = storageService;
    this.promotionService = promotionService;
    this.rentalMapper = rentalMapper;
    this.rentalDetailRepository = rentalDetailRepository;
    this.roomFactory = roomFactory;
    this.commonService = commonService;
    this.amenityRepository = amenityRepository;
    this.cacheManager = cacheManager;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isSuiteIdExist(int suiteId) {
    return suiteRepository.existsById(suiteId);
  }

  @Override
  @LogAround(output = false)
  @Transactional(readOnly = true)
  public List<SuiteDto> getBestSaleSuites(final Integer nSuite) {
    return suiteRepository.findTopNBestSaleSuites(
        nSuite != null ? nSuite : DEFAULT_BEST_SALE_SUITES_NUM);
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public boolean isRoomExistById(final int roomId) {
    return roomRepository.existsById(roomId);
  }

  @Override
  @LogAround(output = false, jsonInput = true)
  @Transactional(readOnly = true)
  public Page<SuiteDto> getSuiteSearchResults(
      final SuiteSearchRequestDto suiteSearchRequestDto, final Pageable pageable) {
    return suiteRepository.findAvailableSuitesForReservation(suiteSearchRequestDto, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Suite getSuiteById(final int suiteId) {
    return suiteRepository.findById(suiteId).orElseThrow(() -> new SuiteNotFoundException(suiteId));
  }

  @Override
  @Transactional(readOnly = true)
  public Room getRoomById(final int roomId) {
    return roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException(roomId));
  }

  @Transactional(readOnly = true)
  public Room getRoomWithSuiteInfoById(final int roomId) {
    return roomRepository
        .findRoomWithSuiteInfoById(roomId)
        .orElseThrow(() -> new RoomNotFoundException(roomId));
  }

  @Override
  @LogAround(output = false)
  @Transactional(readOnly = true)
  public List<RoomDto> getAllRoomsWithBasicInfo(
      final RoomMapFilterOptionDTO roomMapFilterOptionDTO) {
    return roomRepository.getAllRoomsWithRentalDetail(roomMapFilterOptionDTO);
  }

  @Override
  @LogAround
  public RoomDto updateStatusOfRoom(final int roomId, final RoomStatusCode roomStatusCode) {
    Objects.requireNonNull(roomStatusCode);

    final Room room = getRoomById(roomId);
    room.setStatus(commonService.getRoomStatusByCode(roomStatusCode));
    return roomMapper.toRoomDto(roomRepository.save(room));
  }

  @Override
  @Transactional(readOnly = true)
  public RoomStatusStatsDto getRoomStatusStatistics() {
    return roomRepository.getRoomStatusStats();
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public RoomDto getDetailOfRoomById(final int roomId) {
    final Room room = getRoomById(roomId);
    final RoomDto dto = roomMapper.toRoomDto(room);

    getRentalDetailOfRoom(room)
        .ifPresent(
            rentalDetail -> dto.setRentalDetail(rentalMapper.toDetailDtoWithoutRoom(rentalDetail)));
    return dto;
  }

  @Override
  @LogAround(output = false)
  @Transactional(readOnly = true)
  public Page<SuitePriceHistoryDto> getSuitePriceHistory(
      final int suiteId, final Pageable pageable) {
    if (!suiteRepository.existsById(suiteId)) {
      throw new SuiteNotFoundException(suiteId);
    }
    return suitePriceHistoryRepository
        .findBySuiteId(suiteId, pageable)
        .map(suiteMapper::toPriceHistoryDto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<SuiteDto> getAllSuites() {
    return suiteRepository.findAll().stream()
        .map(suiteMapper::toBasicDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public SuiteDto getDetailOfSuite(final int suiteId) {
    final Suite suite = getSuiteById(suiteId);
    return suiteMapper
        .toBasicDto(suite)
        .setRooms(roomMapper.toRoomBasicDtoWithoutSuite(roomRepository.findBySuite(suite)));
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  @Cacheable(CACHE_ROOM_FLOOR_NUMBERS)
  public List<Integer> getFoorNumbers() {
    return roomRepository.getFloorNumbers();
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public boolean isRoomNameDuplicated(final String name) {
    Objects.requireNonNull(name);
    return roomRepository.existsByName(name);
  }

  @Override
  @LogAround
  public RoomDto updateRoom(final int roomId, final RoomUpsertDto roomUpsertDto) {
    Objects.requireNonNull(roomUpsertDto);
    final Room room = getRoomById(roomId);

    final String roomName = roomUpsertDto.getName().trim();
    boolean isNameDuplicated = !room.getName().equals(roomName) && isRoomNameDuplicated(roomName);
    if (isNameDuplicated) {
      throw InvalidParamException.builder()
          .body(ROOM_NAME_DUPLICATED, "name", roomName, "Room name is duplicated.")
          .build();
    }

    roomMapper.partialUpdate(room, roomUpsertDto);
    clearCacheRoomFloorNumbers();
    return roomMapper.toRoomDto(roomRepository.save(room));
  }

  @Override
  @LogAround
  public RoomDto updateRoomAvailability(final int roomId, final boolean available) {
    final Room room = getRoomById(roomId);
    room.setAvailable(available);
    return roomMapper.toRoomBasicDtoWithoutSuite(roomRepository.save(room));
  }

  @Override
  @LogAround
  public SuiteStyleDto saveNewSuiteStyle(final SuiteStyleUpsertDto suiteStyleUpsertDto) {
    Objects.requireNonNull(suiteStyleUpsertDto);

    if (suiteStyleRepository.existsByName(suiteStyleUpsertDto.getName())) {
      throw new ConstraintViolationException("suiteStyle", "name", "unique");
    }

    final SuiteStyle newStyle = new SuiteStyle();
    suiteMapper.partialUpdateSuiteStyle(suiteStyleUpsertDto, newStyle);
    return suiteMapper.toSuiteStyleDto(suiteStyleRepository.save(newStyle));
  }

  @Override
  @LogAround
  public SuiteStyleDto updateSuiteStyle(
      final int suiteStyleId, final SuiteStyleUpsertDto suiteStyleUpsertDto) {
    Objects.requireNonNull(suiteStyleUpsertDto);

    final SuiteStyle suiteStyle = commonService.getSuiteStyleById(suiteStyleId);

    if (!suiteStyle.getName().equalsIgnoreCase(suiteStyleUpsertDto.getName())
        && suiteStyleRepository.existsByName(suiteStyleUpsertDto.getName())) {
      throw new SuiteTypeNameUniqueException();
    }

    suiteMapper.partialUpdateSuiteStyle(suiteStyleUpsertDto, suiteStyle);
    commonService.clearCache();
    return suiteMapper.toSuiteStyleDto(suiteStyleRepository.save(suiteStyle));
  }

  @Override
  @LogAround
  public SuiteTypeDto saveNewSuiteType(final SuiteTypeUpsertDto suiteTypeUpsertDto) {
    Objects.requireNonNull(suiteTypeUpsertDto);

    if (suiteTypeRepository.existsByName(suiteTypeUpsertDto.getName())) {
      throw new SuiteTypeNameUniqueException();
    }

    final Set<Amenity> amenities = amenityRepository.findByIdIn(suiteTypeUpsertDto.getAmenityIds());
    final SuiteType newType =
        new SuiteType().setName(suiteTypeUpsertDto.getName()).addAmenities(amenities);

    return suiteMapper.toSuiteTypeDto(suiteTypeRepository.save(newType));
  }

  @Override
  @LogAround
  public SuiteTypeDto updateSuiteType(
      final int suiteTypeId, final SuiteStyleUpsertDto suiteStyleUpsertDto) {
    Objects.requireNonNull(suiteStyleUpsertDto);

    final SuiteType suiteType = commonService.getSuiteTypeById(suiteTypeId);

    if (!suiteType.getName().equalsIgnoreCase(suiteStyleUpsertDto.getName())
        && suiteTypeRepository.existsByName(suiteStyleUpsertDto.getName())) {
      throw new SuiteTypeNameUniqueException();
    }

    suiteMapper.partialUpdateSuiteType(suiteStyleUpsertDto, suiteType);
    commonService.clearCache();
    return suiteMapper.toSuiteTypeDto(suiteTypeRepository.save(suiteType));
  }

  @Override
  @LogAround
  public SuiteDto updateSuite(final int suiteId, final SuiteUpdateDto suiteUpdateDto) {
    Objects.requireNonNull(suiteUpdateDto);

    final Suite suite = getSuiteById(suiteId);

    final boolean isPriceChanged =
        (suiteUpdateDto.getPrice() != null && suite.getOriginalPrice() != suiteUpdateDto.getPrice())
            || (suiteUpdateDto.getVat() != null && suite.getVat() != suiteUpdateDto.getVat());

    if (isPriceChanged) {
      saveNewSuitePriceHistory(suite, suiteUpdateDto.getPrice(), suiteUpdateDto.getVat());
    }

    suiteMapper.partialUpdateSuite(suiteUpdateDto, suite);
    clearSuiteCache(suite);
    return suiteMapper.toDto(suiteRepository.save(suite));
  }

  @Override
  @LogAround
  public SuiteDto updateSuiteAvailability(final int suiteId, final boolean available) {
    final Suite suite = getSuiteById(suiteId);
    suite.setAvailable(available);
    for (final Room room : suite.getRooms()) {
      room.setAvailable(suite.isAvailable());
    }
    return suiteMapper.toDto(suiteRepository.save(suite));
  }

  @Override
  @LogAround(input = false)
  public SuiteDto updateSuiteImage(final int suiteId, final MultipartFile multipartFile) {
    Objects.requireNonNull(multipartFile);

    final Suite suite = getSuiteById(suiteId);
    final String fileName = suite.getId() + FileUtil.getFileExtension(multipartFile);

    if (suite.getImageURL() != null) {
      storageService.deleteFiles(List.of(suite.getImageURL()));
    }

    suite.setImageURL(
        storageService.saveFile(
            multipartFile, new StorageService.FileEntry(fileName, UPLOAD_IMG, SUITE)));
    clearSuiteCache(suite);
    return suiteMapper.toDto(suiteRepository.save(suite));
  }

  @Override
  @LogAround
  public SuiteDto addNewRoomsForSuite(final int suiteId, final List<RoomUpsertDto> rooms) {
    final Suite suite = getSuiteById(suiteId);
    final List<Room> newRooms = new ArrayList<>();

    for (final RoomUpsertDto roomUpsertDto : rooms) {
      if (roomRepository.existsByName(roomUpsertDto.getName())) {
        throw new ConstraintViolationException("room", "name", "unique");
      }

      newRooms.add(
          roomFactory.createNewRoom(
              roomUpsertDto.getName(),
              roomUpsertDto.getFloor(),
              suite.isAvailable(),
              RoomStatusCode.EMPTY));
    }

    newRooms.forEach(suite::addRoom);
    clearSuiteCache(suite);
    clearCacheRoomFloorNumbers();
    return suiteMapper.toDto(suiteRepository.save(suite));
  }

  public void clearCacheRoomFloorNumbers() {
    Objects.requireNonNull(cacheManager.getCache(CACHE_ROOM_FLOOR_NUMBERS)).clear();
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isRoomBelongToSuite(final int roomId, final int suiteId) {
    return roomRepository.existsByIdAndSuiteId(roomId, suiteId);
  }

  @Override
  @LogAround
  public SuiteDto saveNewSuite(final SuiteCreateDto suiteCreateDto, final MultipartFile imageFile) {
    Objects.requireNonNull(suiteCreateDto);
    Objects.requireNonNull(imageFile);

    if (suiteRepository.existsBySuiteStyleIdAndSuiteTypeId(
        suiteCreateDto.getSuiteStyleId(), suiteCreateDto.getSuiteTypeId())) {
      throw new SuiteIllegalUpdateException("pairTypeStyle", "unique");
    }

    Suite newSuite = roomFactory.createNewSuite(suiteCreateDto);
    newSuite = suiteRepository.save(newSuite);

    final String imageFilename = newSuite.getId() + FileUtil.getFileExtension(imageFile);

    newSuite.setImageURL(
        storageService.saveFile(
            imageFile, new StorageService.FileEntry(imageFilename, UPLOAD_IMG, SUITE)));

    saveNewSuitePriceHistory(newSuite, newSuite.getOriginalPrice(), newSuite.getVat());
    return suiteMapper.toBasicDto(suiteRepository.save(newSuite));
  }

  @Override
  public void clearSuiteCache(final Suite suite) {
    log.debug("Clear suite cache by id '{}'", suite.getId());
    Objects.requireNonNull(cacheManager.getCache(Suite.CACHE)).evict(suite.getId());
  }

  private Optional<RentalDetail> getRentalDetailOfRoom(final Room room) {
    return rentalDetailRepository
        .findCurrentRentalDetailOfRoomById(room.getId())
        .map(
            detail -> {
              detail.setPromotions(
                  promotionService.getSuitePromotionsFromDate(
                      room.getSuiteId(),
                      detail.getCheckInAt().toLocalDate(),
                      detail.getCheckOutAt().toLocalDate()));
              return detail;
            });
  }

  private SuitePriceHistoryDto saveNewSuitePriceHistory(
      final Suite suite, final int newPrice, final int newVat) {
    if (suitePriceHistoryRepository.existsById(new SuitePriceHistoryId(suite, LocalDate.now()))) {
      throw new SuitePriceUpdateNotAllowedException("Update price is allowed once a day");
    }
    log.debug(
        "Save suite price update [suite id: '{}', new price: '{}', new vat: '{}']",
        suite.getId(),
        newPrice,
        newVat);
    final SuitePriceHistory newSuitePrice =
        roomFactory.createSuitePriceHistory(suite, newPrice, newVat);
    return suiteMapper.toPriceHistoryDto(suitePriceHistoryRepository.save(newSuitePrice));
  }
}