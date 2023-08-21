package vn.lotusviet.hotelmgmt.service;

import org.springframework.web.multipart.MultipartFile;
import vn.lotusviet.hotelmgmt.model.dto.ads.*;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;

import java.time.LocalDate;
import java.util.List;

public interface PromotionService {

  // =========================================================================
  // PROMOTION
  // =========================================================================

  List<PromotionDto> getSuitePromotionsFromDate(
      int suiteId, LocalDate checkInAt, LocalDate checkOutAt);

  List<ManagedPromotionDto> getAllPromotions();

  ManagedPromotionDto saveNewPromotion(PromotionCreateDto promotionCreateDto);

  ManagedPromotionDto getDetailOfPromotion(long id);

  ManagedPromotionDto updatePromotion(long id, PromotionUpdateDto promotionUpdateDto);

  List<SuiteDto> getSuiteOptions(LocalDate dateFrom, LocalDate dateTo);

  // =========================================================================
  // PROGRAMS
  // =========================================================================

  List<PromotionProgramDto> getPromotionPrograms();

  PromotionProgramDto getDetailOfPromotionProgram(int id);

  PromotionProgramDto saveNewPromotionProgram(
      PromotionProgramUpsertDto promotionProgramUpsertDto, MultipartFile imageFile);

  PromotionProgramDto updatePromotionProgram(
      int programId, PromotionProgramUpsertDto promotionProgramUpsertDto, MultipartFile imageFile);

  PromotionProgramDto softDeletePromotionProgram(int programId);

  void hardDeletePromotionProgram(int programId);

  PromotionProgramDto restoreDeletedPromotionProgram(int programId);

  PromotionProgramDto setVisiblePromotionProgram(int programId, boolean visible);
}