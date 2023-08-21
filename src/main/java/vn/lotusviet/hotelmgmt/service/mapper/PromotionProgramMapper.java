package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.*;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionProgramDto;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionProgramUpsertDto;
import vn.lotusviet.hotelmgmt.model.entity.ads.PromotionProgram;
import vn.lotusviet.hotelmgmt.service.mapper.decorator.PromotionProgramMapperDecorator;

@Mapper(componentModel = "spring")
@DecoratedWith(PromotionProgramMapperDecorator.class)
public interface PromotionProgramMapper {

  String TO_DTO_QUALIFIER = "toDto";
  String TO_DTO_WITHOUT_CONTENT_QUALIFIER = "toDtoWithoutContent";

  @Named(TO_DTO_QUALIFIER)
  PromotionProgramDto toDto(PromotionProgram entity);

  @Named(TO_DTO_WITHOUT_CONTENT_QUALIFIER)
  @Mapping(target = "content", ignore = true)
  PromotionProgramDto toDtoWithoutContent(PromotionProgram entity);

  @Mapping(target = "id", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void partialUpdate(@MappingTarget PromotionProgram entity, PromotionProgramDto dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void partialUpdate(@MappingTarget PromotionProgram entity, PromotionProgramUpsertDto dto);
}