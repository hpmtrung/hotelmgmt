package vn.lotusviet.hotelmgmt.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.lotusviet.hotelmgmt.model.dto.invoice.CommonInvoiceDto;
import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceDetailDto;
import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceDto;
import vn.lotusviet.hotelmgmt.model.dto.invoice.MergedInvoiceDto;
import vn.lotusviet.hotelmgmt.model.entity.rental.Invoice;
import vn.lotusviet.hotelmgmt.model.entity.rental.MergedInvoice;
import vn.lotusviet.hotelmgmt.model.entity.rental.Rental;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;

import java.util.List;
import java.util.Objects;

@Mapper(
    componentModel = "spring",
    uses = {EmployeeMapper.class, CustomerMapper.class, ServiceMapper.class})
public interface InvoiceMapper {

  String TO_DTO_DEPOSIT_AMOUNT_QUALIFIER = "toInvoiceDto_depositAmount";
  String TO_DTO_DISCOUNT_AMOUNT_QUALIFIER = "toInvoiceDto_discountAmount";

  @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = EmployeeMapper.TO_DTO_WITH_ID_AND_NAME_QUALIFIER)
  MergedInvoiceDto toMergedInvoiceDto(MergedInvoice entity);

  @Mapping(
      target = "createdBy",
      source = "createdBy",
      qualifiedByName = EmployeeMapper.TO_DTO_WITH_ID_AND_NAME_QUALIFIER)
  CommonInvoiceDto toCommonInvoice(Invoice entity);

  List<CommonInvoiceDto> toCommonInvoice(List<Invoice> entities);

  @Mapping(
      target = "createdBy",
      source = "createdBy",
      qualifiedByName = EmployeeMapper.TO_DTO_WITH_ID_AND_NAME_QUALIFIER)
  CommonInvoiceDto toCommonInvoiceFromMerge(MergedInvoice entity);

  List<CommonInvoiceDto> toCommonInvoiceFromMerge(List<MergedInvoice> entities);

  @Mapping(target = "paymentMethodName", source = "entity.paymentMethod.name")
  @Mapping(
      target = "createdBy",
      source = "entity.createdBy",
      qualifiedByName = EmployeeMapper.TO_DTO_WITH_ID_AND_NAME_QUALIFIER)
  @Mapping(
      target = "depositAmount",
      source = "entity",
      qualifiedByName = TO_DTO_DEPOSIT_AMOUNT_QUALIFIER)
  @Mapping(
      target = "discountAmount",
      source = "entity",
      qualifiedByName = TO_DTO_DISCOUNT_AMOUNT_QUALIFIER)
  @Mapping(target = "details", source = "entity.rentalDetails")
  @Mapping(target = "owner", source = "entity.rental.owner")
  InvoiceDto toInvoiceDto(Invoice entity);

  @Mapping(target = "rentalDetailId", source = "id")
  @Mapping(target = "roomName", source = "room.name")
  @Mapping(target = "serviceUsageDetails", source = "serviceUsageDetailsOfInvoice")
  InvoiceDetailDto toInvoiceDetailDto(RentalDetail entity);

  MergedInvoiceDto.MergedInvoiceRentalDto toMergedInvoiceRentalDto(Rental entity);

  @Named(TO_DTO_DEPOSIT_AMOUNT_QUALIFIER)
  default int toInvoiceDto_depositAmount(final Invoice invoice) {
    if (!invoice.getDepositUsed()) return 0;
    return Objects.requireNonNull(invoice.getRental().getReservation()).getDepositAmount();
  }

  @Named(TO_DTO_DISCOUNT_AMOUNT_QUALIFIER)
  default int toInvoiceDto_discountAmount(final Invoice invoice) {
    if (!invoice.getRentalDiscountUsed()) return 0;
    return invoice.getRental().getDiscountAmount();
  }
}