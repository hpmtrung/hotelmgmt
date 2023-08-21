package vn.lotusviet.hotelmgmt.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.exception.*;
import vn.lotusviet.hotelmgmt.model.dto.ads.*;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;
import vn.lotusviet.hotelmgmt.model.entity.ads.Promotion;
import vn.lotusviet.hotelmgmt.model.entity.ads.PromotionDetail;
import vn.lotusviet.hotelmgmt.model.entity.ads.PromotionProgram;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;
import vn.lotusviet.hotelmgmt.repository.ads.PromotionProgramRepository;
import vn.lotusviet.hotelmgmt.repository.ads.PromotionRepository;
import vn.lotusviet.hotelmgmt.repository.room.SuiteRepository;
import vn.lotusviet.hotelmgmt.service.PromotionService;
import vn.lotusviet.hotelmgmt.service.mapper.PromotionMapper;
import vn.lotusviet.hotelmgmt.service.mapper.PromotionProgramMapper;
import vn.lotusviet.hotelmgmt.service.storage.StorageService;
import vn.lotusviet.hotelmgmt.service.storage.StorageService.FileEntry;
import vn.lotusviet.hotelmgmt.util.DatetimeUtil;
import vn.lotusviet.hotelmgmt.util.DatetimeUtil.DateRange;
import vn.lotusviet.hotelmgmt.util.FileUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static vn.lotusviet.hotelmgmt.service.storage.StorageService.FileBrand.UPLOAD_IMG;
import static vn.lotusviet.hotelmgmt.service.storage.StorageService.FileCategory.PROMOTION_PROGRAM;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

  public static final String CACHE_SUITE_PROMOTION = "CacheSuitePromotionByIdAndRentalDetailDate";

  private static final Logger log = LoggerFactory.getLogger(PromotionServiceImpl.class);

  private final PromotionRepository promotionRepository;
  private final PromotionMapper promotionMapper;
  private final SuiteRepository suiteRepository;
  private final PromotionProgramRepository promotionProgramRepository;
  private final PromotionProgramMapper promotionProgramMapper;
  private final StorageService storageService;
  private final CacheManager cacheManager;

  public PromotionServiceImpl(
      PromotionRepository promotionRepository,
      PromotionMapper promotionMapper,
      SuiteRepository suiteRepository,
      PromotionProgramRepository promotionProgramRepository,
      PromotionProgramMapper promotionProgramMapper,
      StorageService storageService,
      CacheManager cacheManager) {
    this.promotionRepository = promotionRepository;
    this.promotionMapper = promotionMapper;
    this.suiteRepository = suiteRepository;
    this.promotionProgramRepository = promotionProgramRepository;
    this.promotionProgramMapper = promotionProgramMapper;
    this.storageService = storageService;
    this.cacheManager = cacheManager;
  }

  @Override
  @LogAround(jsonInput = true, jsonOutput = true)
  public ManagedPromotionDto saveNewPromotion(final PromotionCreateDto promotionCreateDto) {
    Objects.requireNonNull(promotionCreateDto);

    if (promotionRepository.existsByCode(promotionCreateDto.getCode())) {
      throw new PromotionCodeUniqueException();
    }
    final Promotion newPromotion = promotionMapper.toPromotionEntity(promotionCreateDto);
    newPromotion.setAvailable(true);
    clearAllSuitePromotionFindingCache();
    return promotionMapper.toManagedDto(promotionRepository.save(newPromotion));
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public ManagedPromotionDto getDetailOfPromotion(final long id) {
    return promotionMapper.toManagedDto(getPromotionById(id));
  }

  @Override
  @LogAround
  public ManagedPromotionDto updatePromotion(
      final long promotionId, final PromotionUpdateDto promotionUpdateDto) {
    Objects.requireNonNull(promotionUpdateDto);

    Promotion promotion = getPromotionById(promotionId);
    final String newCode = promotionUpdateDto.getCode();
    boolean changed = false;

    if (newCode != null) {
      if (newCode.isBlank()) {
        throw new RequestParamInvalidException("Promotion code is invalid", "code", "invalid");
      }

      promotion.setCode(newCode.trim());
      changed = true;
    }

    if (promotionUpdateDto.getAvailable() != null) {
      promotion.setAvailable(promotionUpdateDto.getAvailable());
      changed = true;
    }

    if (promotionUpdateDto.getAddedDetails() != null) {
      final List<PromotionDetail> newDetails = new ArrayList<>();
      for (final PromotionUpsertDetailDto detail : promotionUpdateDto.getAddedDetails()) {
        final Suite suite =
            suiteRepository
                .findById(detail.getSuiteId())
                .orElseThrow(() -> new SuiteNotFoundException(detail.getSuiteId()));
        newDetails.add(
            new PromotionDetail().setSuite(suite).setDiscountPercent(detail.getDiscountPercent()));
      }

      newDetails.forEach(promotion::addDetail);
    }

    if (changed) {
      clearPromotionCache(promotion);
      clearAllSuitePromotionFindingCache();
      promotion = promotionRepository.save(promotion);
    }
    return promotionMapper.toManagedDto(promotion);
  }

  @Override
  @LogAround
  @Transactional(readOnly = true)
  public List<SuiteDto> getSuiteOptions(final LocalDate dateFrom, final LocalDate dateTo) {
    Objects.requireNonNull(dateFrom);
    Objects.requireNonNull(dateTo);
    return promotionRepository.findSuitesHasNoPromotionAtDateRange(dateFrom, dateTo);
  }

  @Override
  @LogAround
  @Cacheable(value = CACHE_SUITE_PROMOTION, condition = "#checkInAt != null && #checkOutAt != null")
  @Transactional(readOnly = true)
  public List<PromotionDto> getSuitePromotionsFromDate(
      final int suiteId, final LocalDate checkInAt, final LocalDate checkOutAt) {
    Objects.requireNonNull(checkInAt);
    Objects.requireNonNull(checkOutAt);

    if (checkInAt.isAfter(checkOutAt)) throw new IllegalArgumentException("Date range is invalid");

    final List<PromotionDto> suitePromotions =
        promotionRepository.findPromotionsOfSuiteByDate(suiteId, checkInAt, checkOutAt);

    log.debug("Find promotions: {}", suitePromotions);

    final List<PromotionDto> result = new ArrayList<>();

    LocalDate dateBeforeCheckout = checkOutAt.minusDays(1);
    if (dateBeforeCheckout.isBefore(checkInAt)) {
      dateBeforeCheckout = checkInAt;
    }

    for (final PromotionDto promotion : suitePromotions) {
      DatetimeUtil.intersect(
              DateRange.of(checkInAt, dateBeforeCheckout),
              DateRange.of(promotion.getStartAt(), promotion.getEndAt()))
          .ifPresent(
              period -> {
                log.debug(
                    "Find accepted promotion [code '{}', period '{}']",
                    promotion.getCode(),
                    period);
                if (isAcceptedPromotionPeriod(period)) {
                  result.add(createPromotionPeriodInfo(promotion, period));
                }
              });
    }
    return result;
  }

  @Override
  @LogAround(output = false)
  @Transactional(readOnly = true)
  public List<ManagedPromotionDto> getAllPromotions() {
    return promotionMapper.toManagedDtos(promotionRepository.findAll());
  }

  @Override
  @LogAround(output = false)
  @Transactional(readOnly = true)
  public List<PromotionProgramDto> getPromotionPrograms() {
    return promotionProgramRepository.findAll().stream()
        .map(promotionProgramMapper::toDtoWithoutContent)
        .collect(Collectors.toList());
  }

  @Override
  @LogAround(output = false)
  @Transactional(readOnly = true)
  public PromotionProgramDto getDetailOfPromotionProgram(final int id) {
    return promotionProgramMapper.toDto(getPromotionProgramById(id));
  }

  @Override
  @LogAround(output = false)
  public PromotionProgramDto saveNewPromotionProgram(
      final PromotionProgramUpsertDto promotionProgramUpsertDto, final MultipartFile imageFile) {
    Objects.requireNonNull(promotionProgramUpsertDto);
    Objects.requireNonNull(imageFile);

    PromotionProgram newProgram = new PromotionProgram();
    promotionProgramMapper.partialUpdate(newProgram, promotionProgramUpsertDto);
    newProgram.setDeleted(false).setCreatedAt(LocalDateTime.now());
    newProgram = promotionProgramRepository.save(newProgram);

    final String fileExtension = FileUtil.getFileExtension(imageFile);
    final String fileName = newProgram.getId() + fileExtension;

    newProgram.setImageURL(
        storageService.saveFile(imageFile, new FileEntry(fileName, UPLOAD_IMG, PROMOTION_PROGRAM)));

    return promotionProgramMapper.toDto(promotionProgramRepository.save(newProgram));
  }

  @Override
  @LogAround(output = false)
  public PromotionProgramDto updatePromotionProgram(
      final int programId,
      final PromotionProgramUpsertDto promotionProgramUpsertDto,
      final MultipartFile imageFile) {
    Objects.requireNonNull(promotionProgramUpsertDto);

    final PromotionProgram program = getPromotionProgramById(programId);
    promotionProgramMapper.partialUpdate(program, promotionProgramUpsertDto);

    if (imageFile != null) {
      final String fileExtension = FileUtil.getFileExtension(imageFile);
      final String fileName = program.getId() + fileExtension;

      storageService.deleteFiles(List.of(program.getImageURL()));

      program.setImageURL(
          storageService.saveFile(
              imageFile, new FileEntry(fileName, UPLOAD_IMG, PROMOTION_PROGRAM)));
    }

    clearPromotionProgramCache(program);
    return promotionProgramMapper.toDto(promotionProgramRepository.save(program));
  }

  @Override
  @LogAround(output = false)
  public PromotionProgramDto softDeletePromotionProgram(final int programId) {
    final PromotionProgram program = getPromotionProgramById(programId);
    if (program.isDeleted()) {
      throw new IllegalStateException("Soft delete program in illegal state");
    }
    program.setDeleted(true);
    clearPromotionProgramCache(program);
    return promotionProgramMapper.toDto(promotionProgramRepository.save(program));
  }

  @Override
  @LogAround
  public void hardDeletePromotionProgram(final int programId) {
    final PromotionProgram program = getPromotionProgramById(programId);
    if (!program.isDeleted()) {
      throw new IllegalStateException("Hard delete program in illegal state");
    }
    storageService.deleteFiles(List.of(program.getImageURL()));
    clearPromotionProgramCache(program);
    promotionProgramRepository.delete(program);
  }

  @Override
  @LogAround(output = false)
  public PromotionProgramDto restoreDeletedPromotionProgram(final int programId) {
    final PromotionProgram program = getPromotionProgramById(programId);
    if (!program.isDeleted()) {
      throw new IllegalArgumentException("Restore undeleted program is not allowed");
    }
    program.setDeleted(false);
    clearPromotionProgramCache(program);
    return promotionProgramMapper.toDto(promotionProgramRepository.save(program));
  }

  @Override
  @LogAround(output = false)
  public PromotionProgramDto setVisiblePromotionProgram(
      final int programId, final boolean visible) {
    final PromotionProgram program = getPromotionProgramById(programId);
    program.setVisible(visible);
    clearPromotionProgramCache(program);
    return promotionProgramMapper.toDto(promotionProgramRepository.save(program));
  }

  private Promotion getPromotionById(final long id) {
    return promotionRepository.findById(id).orElseThrow(() -> new PromotionNotFoundException(id));
  }

  private PromotionProgram getPromotionProgramById(final int id) {
    return promotionProgramRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("promotionProgram", String.valueOf(id)));
  }

  private boolean isAcceptedPromotionPeriod(final DateRange<LocalDate> period) {
    return period.getFrom().until(period.getTo(), ChronoUnit.DAYS) >= 0;
  }

  private PromotionDto createPromotionPeriodInfo(
      final PromotionDto promotion, final DateRange<LocalDate> period) {
    return new PromotionDto()
        .setDiscountPercent(promotion.getDiscountPercent())
        .setStartAt(period.getFrom())
        .setEndAt(period.getTo())
        .setCode(promotion.getCode())
        .setId(promotion.getId());
  }

  private void clearPromotionCache(final Promotion promotion) {
    log.debug("Clear promotion cache by id '{}'", promotion.getId());
    Objects.requireNonNull(cacheManager.getCache(Promotion.CACHE)).evict(promotion.getId());
    clearAllSuitePromotionFindingCache();
  }

  private void clearAllSuitePromotionFindingCache() {
    log.debug("Clear all of suite promotion finding cache");
    Objects.requireNonNull(cacheManager.getCache(CACHE_SUITE_PROMOTION)).clear();
  }

  private void clearPromotionProgramCache(final PromotionProgram promotionProgram) {
    log.debug("Clear promotion program cache by id '{}'", promotionProgram.getId());
    Objects.requireNonNull(cacheManager.getCache(PromotionProgram.CACHE))
        .evict(promotionProgram.getId());
  }
}