package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.lotusviet.hotelmgmt.model.dto.ads.ManagedPromotionDto;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionCreateDto;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.model.entity.ads.Promotion;
import vn.lotusviet.hotelmgmt.model.entity.ads.PromotionDetail;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {SuiteMapper.class})
public interface PromotionMapper {

  @Mapping(target = "discountPercent", ignore = true)
  PromotionDto toDto(Promotion entity);

  List<PromotionDto> toDto(List<Promotion> entities);

  @Mapping(
      target = "suite",
      source = "suite",
      qualifiedByName = SuiteMapper.TO_SUITE_DTO_WITH_TYPE_STYLE_PRICE_QUALIFIER)
  ManagedPromotionDto.AppliedSuiteDto toAppliedSuiteDto(PromotionDetail entity);

  ManagedPromotionDto toManagedDto(Promotion promotion);

  List<ManagedPromotionDto> toManagedDtos(List<Promotion> promotions);

  Promotion toPromotionEntity(PromotionCreateDto promotionCreateDto);
}