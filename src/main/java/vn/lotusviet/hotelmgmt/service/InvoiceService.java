package vn.lotusviet.hotelmgmt.service;

import vn.lotusviet.hotelmgmt.model.dto.invoice.CommonInvoiceDto;
import vn.lotusviet.hotelmgmt.model.dto.invoice.CommonInvoiceFilterDto;
import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceDto;
import vn.lotusviet.hotelmgmt.model.dto.invoice.MergedInvoiceDto;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceService {

  List<MergedInvoiceDto.MergedInvoiceRentalDto> getRentalNotHavingMergedInvoices(
      LocalDate dateFrom, LocalDate dateTo);

  List<CommonInvoiceDto> getCommonInvoices(CommonInvoiceFilterDto filterDto);

  MergedInvoiceDto mergeInvoice(List<Long> rentalIds);

  MergedInvoiceDto mergeInvoicePreview(List<Long> rentalIds);

  InvoiceDto getInvoiceById(long invoiceId);

  MergedInvoiceDto getMergedInvoiceById(long invoiceId);
}