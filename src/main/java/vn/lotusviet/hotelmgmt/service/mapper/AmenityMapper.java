package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.Mapper;
import vn.lotusviet.hotelmgmt.model.dto.room.AmenityDto;
import vn.lotusviet.hotelmgmt.model.entity.room.Amenity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AmenityMapper {
  AmenityDto toAmentityDto(Amenity entity);

  List<AmenityDto> toAmentityDto(List<Amenity> entities);
}