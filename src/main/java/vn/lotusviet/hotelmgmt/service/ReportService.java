package vn.lotusviet.hotelmgmt.service;

import vn.lotusviet.hotelmgmt.model.dto.invoice.InvoiceDto;
import vn.lotusviet.hotelmgmt.model.dto.invoice.MergedInvoiceDto;
import vn.lotusviet.hotelmgmt.model.dto.report.TurnOverYearReportDto;

import java.time.LocalDate;

public interface ReportService {

  TurnOverYearReportDto getTurnOverOfYear(int year);

  byte[] getTurnOverYearReportXlsx(int year);

  byte[] getReservationMonthStatsReportPdf(LocalDate dateFrom, LocalDate dateTo);

  byte[] getRoomOccupancyReportPdf(LocalDate dateFrom, LocalDate dateTo);

  byte[] getSuiteTurnOverStatsReportPdf(LocalDate dateFrom, LocalDate dateTo);

  byte[] getInvoicePdf(InvoiceDto invoice);

  byte[] getMergedInvoicePdf(MergedInvoiceDto mergedInvoiceDto);
}