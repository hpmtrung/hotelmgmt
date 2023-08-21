package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationDetailDto;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationDto;
import vn.lotusviet.hotelmgmt.model.entity.reservation.Reservation;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationDetail;

@Mapper(
    componentModel = "spring",
    uses = {EmployeeMapper.class, CustomerMapper.class, AccountMapper.class, SuiteMapper.class})
public interface ReservationMapper {

  String TO_BASIC_DTO_QUALIFIER = "toBasicDto";
  String TO_DTO_QUALIFIER = "toDto";

  @Mapping(
      target = "suite",
      source = "suite",
      qualifiedByName = SuiteMapper.TO_DTO_FOR_RESERVATION_QUALIFIER)
  ReservationDetailDto toDetailDto(ReservationDetail entity);

  @Named(TO_DTO_QUALIFIER)
  @Mapping(target = "paymentMethodCode", source = "paymentMethod.code")
  @Mapping(target = "statusCode", source = "status.code")
  @Mapping(target = "rentalId", source = "rental.id")
  ReservationDto toDto(Reservation entity);

  @Mapping(target = "paymentMethodCode", source = "paymentMethod.code")
  @Mapping(target = "statusCode", source = "status.code")
  @Mapping(target = "owner", ignore = true)
  @Mapping(target = "rentalId", ignore = true)
  ReservationDto toReservationDtoWithoutOwnerInfo(Reservation entity);

  @Named(TO_BASIC_DTO_QUALIFIER)
  @Mapping(target = "paymentMethodCode", source = "paymentMethod.code")
  @Mapping(target = "statusCode", source = "status.code")
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "details", ignore = true)
  @Mapping(target = "rentalId", ignore = true)
  ReservationDto toBasicDto(Reservation entity);

  @Mapping(target = "paymentMethodCode", source = "paymentMethod.code")
  @Mapping(target = "statusCode", source = "status.code")
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "details", ignore = true)
  @Mapping(target = "owner", ignore = true)
  @Mapping(target = "rentalId", ignore = true)
  ReservationDto toBasicReservationDtoWithoutOwnerInfo(Reservation entity);
}