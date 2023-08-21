package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.lotusviet.hotelmgmt.model.dto.newsletter.NewsletterDto;
import vn.lotusviet.hotelmgmt.model.dto.newsletter.SubscriberDto;
import vn.lotusviet.hotelmgmt.model.entity.newsletter.Newsletter;
import vn.lotusviet.hotelmgmt.model.entity.newsletter.Subscriber;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {EmployeeMapper.class})
public interface NewsletterMapper {

  @Named("toNewsletterDto")
  @Mapping(target = "createdBy", qualifiedByName = EmployeeMapper.TO_DTO_WITH_ID_AND_NAME_QUALIFIER)
  NewsletterDto toNewsletterDto(Newsletter entity);

  @Named("toNewsletterDtoWithoutContent")
  @Mapping(target = "content", ignore = true)
  @Mapping(target = "createdBy", qualifiedByName = EmployeeMapper.TO_DTO_WITH_ID_AND_NAME_QUALIFIER)
  NewsletterDto toNewsletterDtoWithoutContent(Newsletter entity);

  @IterableMapping(qualifiedByName = "toNewsletterDtoWithoutContent")
  List<NewsletterDto> toNewsletterDtoWithoutContent(List<Newsletter> entity);

  @IterableMapping(qualifiedByName = "toNewsletterDto")
  List<NewsletterDto> toNewsletterDto(List<Newsletter> entities);

  SubscriberDto toSubcriberDto(Subscriber entity);

  List<SubscriberDto> toSubcriberDtos(List<Subscriber> entities);
}