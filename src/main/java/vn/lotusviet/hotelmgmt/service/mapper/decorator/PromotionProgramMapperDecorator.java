package vn.lotusviet.hotelmgmt.service.mapper.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionProgramDto;
import vn.lotusviet.hotelmgmt.model.entity.ads.PromotionProgram;
import vn.lotusviet.hotelmgmt.service.mapper.PromotionProgramMapper;
import vn.lotusviet.hotelmgmt.service.storage.StorageService;

public abstract class PromotionProgramMapperDecorator implements PromotionProgramMapper {

  @Autowired protected ApplicationProperties applicationProperties;
  @Autowired protected StorageService storageService;

  @Autowired
  @Qualifier("delegate")
  private PromotionProgramMapper delegate;

  @Override
  public PromotionProgramDto toDto(PromotionProgram entity) {
    PromotionProgramDto dto = delegate.toDto(entity);
    return dto.setImageURL(convertImageURL(dto.getImageURL()));
  }

  @Override
  public PromotionProgramDto toDtoWithoutContent(PromotionProgram entity) {
    PromotionProgramDto dto = delegate.toDtoWithoutContent(entity);
    return dto.setImageURL(convertImageURL(dto.getImageURL()));
  }

  private String convertImageURL(String imageKey) {
    return imageKey != null ? storageService.getFileURL(imageKey) : null;
  }
}