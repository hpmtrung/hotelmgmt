package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalBasicDto;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalDetailDto;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalDto;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalPaymentDto;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalPayment;

import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = {
      ReservationMapper.class,
      CustomerMapper.class,
      ServiceMapper.class,
      EmployeeMapper.class,
      RoomMapper.class
    })
public interface RentalMapper {

  String TO_DTO_QUALIFIER = "toDto";
  String TO_DETAIL_DTO_WITHOUT_RENTAL_INFO_QUALIFIER = "todetaildtowithoutrentalInfo";

  @Mapping(target = "room", ignore = true)
  RentalDetailDto toDetailDtoWithoutRoom(RentalDetail entity);

  @Mapping(target = "room", qualifiedByName = RoomMapper.TO_ROOM_BASIC_DTO_QUALIFIER)
  RentalDetailDto toDetailDtoWithBasicRoom(RentalDetail entity);

  @Mapping(target = "rental", ignore = true)
  @Mapping(target = "room", qualifiedByName = RoomMapper.TO_ROOM_BASIC_DTO_QUALIFIER)
  @Named(TO_DETAIL_DTO_WITHOUT_RENTAL_INFO_QUALIFIER)
  RentalDetailDto toDetailDtoWithoutRentalInfo(RentalDetail entity);

  @IterableMapping(qualifiedByName = TO_DETAIL_DTO_WITHOUT_RENTAL_INFO_QUALIFIER)
  List<RentalDetailDto> toDetailDtoWithoutRentalInfo(List<RentalDetail> entity);

  @Named(TO_DTO_QUALIFIER)
  @Mapping(target = "reservation", qualifiedByName = ReservationMapper.TO_DTO_QUALIFIER)
  @Mapping(target = "status", source = "status.code")
  @Mapping(target = "createdBy", qualifiedByName = EmployeeMapper.TO_DTO_WITH_ID_AND_NAME_QUALIFIER)
  @Mapping(target = "paidDetails", ignore = true)
  @Mapping(target = "notPaidDetails", ignore = true)
  RentalDto toDto(Rental entity);

  @IterableMapping(qualifiedByName = TO_DTO_QUALIFIER)
  List<RentalDto> toDto(List<Rental> entity);

  @Mapping(target = "createdBy", qualifiedByName = EmployeeMapper.TO_DTO_WITH_ID_AND_NAME_QUALIFIER)
  @Mapping(target = "status", source = "status.code")
  RentalBasicDto toBasicDto(Rental entity);

  List<RentalBasicDto> toBasicDto(List<Rental> entity);

  @Mapping(
      target = "createdBy",
      source = "createdBy",
      qualifiedByName = EmployeeMapper.TO_DTO_WITH_ID_AND_NAME_QUALIFIER)
  RentalPaymentDto toRentalPaymentDto(RentalPayment entity);
}