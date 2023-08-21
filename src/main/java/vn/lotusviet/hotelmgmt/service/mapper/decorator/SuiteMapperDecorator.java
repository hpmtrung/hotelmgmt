package vn.lotusviet.hotelmgmt.service.mapper.decorator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;
import vn.lotusviet.hotelmgmt.model.entity.room.Amenity;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;
import vn.lotusviet.hotelmgmt.repository.ads.PromotionRepository;
import vn.lotusviet.hotelmgmt.service.mapper.SuiteMapper;
import vn.lotusviet.hotelmgmt.service.storage.StorageService;

import java.util.stream.Collectors;

public abstract class SuiteMapperDecorator implements SuiteMapper {

  @Autowired protected PromotionRepository promotionRepository;
  @Autowired protected StorageService storageService;

  @Autowired
  @Qualifier("delegate")
  private SuiteMapper delegate;

  @Override
  public SuiteDto toDtoForReservation(Suite entity) {
    SuiteDto dto = delegate.toDtoForReservation(entity);
    dto.setImageURL(convertImageURL(dto.getImageURL()));
    dto.setPromotion(getPromotionOfSuite(dto.getId()));
    dto.setAmenityIds(
        entity.getSuiteType().getAmenities().stream()
            .map(Amenity::getId)
            .collect(Collectors.toList()));
    return dto;
  }

  @Override
  public SuiteDto toDto(Suite entity) {
    SuiteDto dto = delegate.toDto(entity);
    dto.setImageURL(convertImageURL(dto.getImageURL()));
    dto.setPromotion(getPromotionOfSuite(dto.getId()));
    return dto;
  }

  @Override
  public SuiteDto toBasicDto(Suite entity) {
    SuiteDto dto = delegate.toBasicDto(entity);
    dto.setImageURL(convertImageURL(dto.getImageURL()));
    dto.setPromotion(getPromotionOfSuite(dto.getId()));
    return dto;
  }

  private String convertImageURL(String imageKey) {
    return imageKey != null ? storageService.getFileURL(imageKey) : null;
  }

  private PromotionDto getPromotionOfSuite(int suiteId) {
    return promotionRepository.findCurrentPromotionOfSuite(suiteId).orElse(null);
  }
}