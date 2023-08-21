package vn.lotusviet.hotelmgmt.web.rest;

import com.vladmihalcea.concurrent.Retry;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.annotation.security.PortalSecured;
import vn.lotusviet.hotelmgmt.core.annotation.validation.image.ValidImage;
import vn.lotusviet.hotelmgmt.exception.PromotionInvalidDateException;
import vn.lotusviet.hotelmgmt.model.dto.ads.*;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;
import vn.lotusviet.hotelmgmt.service.PromotionService;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/promotion")
public class PromotionController {

  private final PromotionService promotionService;

  public PromotionController(PromotionService promotionService) {
    this.promotionService = promotionService;
  }

  @GetMapping
  @PortalSecured
  public List<ManagedPromotionDto> getAllPromotions() {
    return promotionService.getAllPromotions();
  }

  @PortalSecured
  @GetMapping("/suite_options")
  public List<SuiteDto> getSuiteOptions(
      final @RequestParam LocalDate dateFrom, final @RequestParam LocalDate dateTo) {
    return promotionService.getSuiteOptions(dateFrom, dateTo);
  }

  @PortalSecured
  @PostMapping("/save")
  @ResponseStatus(HttpStatus.CREATED)
  public ManagedPromotionDto savePromotion(
      final @Valid @RequestBody PromotionCreateDto promotionCreateDto) {
    if (!promotionCreateDto.getEndAt().isAfter(promotionCreateDto.getStartAt())) {
      throw new PromotionInvalidDateException();
    }
    return promotionService.saveNewPromotion(promotionCreateDto);
  }

  @PortalSecured
  @GetMapping("/detail/{id}")
  public ManagedPromotionDto getPromotionDetail(final @PathVariable long id) {
    return promotionService.getDetailOfPromotion(id);
  }

  @PortalSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/detail/{id}/update")
  public ManagedPromotionDto updatePromotion(
      final @PathVariable long id,
      final @Valid @RequestBody PromotionUpdateDto promotionUpdateDto) {
    return promotionService.updatePromotion(id, promotionUpdateDto);
  }

  @LogAround(output = false)
  @GetMapping("/programs")
  public List<PromotionProgramDto> getPromotionPrograms() {
    return promotionService.getPromotionPrograms();
  }

  @GetMapping("/programs/{programId}")
  public PromotionProgramDto getDetailOfPromotionProgram(final @PathVariable int programId) {
    return promotionService.getDetailOfPromotionProgram(programId);
  }

  @PortalSecured
  @PostMapping("/programs/create")
  public PromotionProgramDto saveNewPromotionProgram(
      final @Valid @RequestPart("data") PromotionProgramUpsertDto promotionProgramUpsertDto,
      final @ValidImage @RequestPart("image") MultipartFile imageFile) {
    return promotionService.saveNewPromotionProgram(promotionProgramUpsertDto, imageFile);
  }

  @PortalSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/programs/{programId}/update")
  public PromotionProgramDto updatePromotionProgram(
      final @PathVariable int programId,
      final @Valid @RequestPart("data") PromotionProgramUpsertDto promotionProgramUpsertDto,
      final @ValidImage @RequestPart(name = "image", required = false) MultipartFile imageFile) {
    return promotionService.updatePromotionProgram(programId, promotionProgramUpsertDto, imageFile);
  }

  @PortalSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/programs/{programId}/soft_delete")
  public PromotionProgramDto softDeletePromotionProgram(final @PathVariable int programId) {
    return promotionService.softDeletePromotionProgram(programId);
  }

  @PortalSecured
  @Retry(on = OptimisticLockException.class)
  @DeleteMapping("/programs/{programId}/hard_delete")
  public void hardDeletePromotionProgram(final @PathVariable int programId) {
    promotionService.hardDeletePromotionProgram(programId);
  }

  @PortalSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/programs/{programId}/restore")
  public PromotionProgramDto restoreDeletedPromotionProgram(final @PathVariable int programId) {
    return promotionService.restoreDeletedPromotionProgram(programId);
  }

  @PortalSecured
  @Retry(on = OptimisticLockException.class)
  @PutMapping("/programs/{programId}/set_visible")
  public PromotionProgramDto setVisibleForPromotionProgram(
      final @PathVariable int programId, final @RequestParam boolean visible) {
    return promotionService.setVisiblePromotionProgram(programId, visible);
  }
}