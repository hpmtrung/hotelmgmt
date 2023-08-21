package vn.lotusviet.hotelmgmt.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties.ReportNameTemplate;
import vn.lotusviet.hotelmgmt.core.annotation.security.PortalSecured;
import vn.lotusviet.hotelmgmt.exception.RequestParamInvalidException;
import vn.lotusviet.hotelmgmt.model.dto.report.TurnOverYearReportDto;
import vn.lotusviet.hotelmgmt.service.ReportService;
import vn.lotusviet.hotelmgmt.util.DatetimeUtil;

import java.time.LocalDate;

@RestController
@PortalSecured
@RequestMapping("/api/v1/report")
public class ReportController extends AbstractController {

  private static final Logger log = LoggerFactory.getLogger(ReportController.class);

  private final ReportService reportService;
  private final ReportNameTemplate reportNameTemplateProperties;

  public ReportController(
      ApplicationProperties applicationProperties, ReportService reportService) {
    this.reportNameTemplateProperties = applicationProperties.getReportNameTemplate();
    this.reportService = reportService;
  }

  @GetMapping("/turn_over")
  public TurnOverYearReportDto getTurnOverCurrYear(final @RequestParam int year) {
    return reportService.getTurnOverOfYear(year);
  }

  @GetMapping("/turn_over/xlsx")
  public ResponseEntity<byte[]> getTurnOverReportXlsx(final @RequestParam int year) {
    final String fileName = String.format("BCDT-%d.xlsx", year);
    return createFileByteResponse(reportService.getTurnOverYearReportXlsx(year), fileName);
  }

  @GetMapping("/room_occupancy/pdf")
  public ResponseEntity<byte[]> getRoomOccupancyReportPdf(
      @RequestParam LocalDate dateFrom, @RequestParam LocalDate dateTo) {
    if (dateFrom.isAfter(dateTo)) {
      throw new RequestParamInvalidException("Date invalid", "date", "invalid");
    }
    dateFrom = DatetimeUtil.atStartOfMonth(dateFrom);
    dateTo = DatetimeUtil.atEndOfMonth(dateTo);

    log.debug("Generate room occupancy report with date: {} to {}", dateFrom, dateTo);

    final String fileName =
        String.format(reportNameTemplateProperties.getRoomOccupancyReport(), dateFrom, dateTo)
            .concat(".pdf");
    return createFileByteResponse(
        reportService.getRoomOccupancyReportPdf(dateFrom, dateTo), fileName);
  }

  @GetMapping("/reservation_month_stats/pdf")
  public ResponseEntity<byte[]> getReservationMonthStatsReport(
      @RequestParam LocalDate dateFrom, @RequestParam LocalDate dateTo) {
    if (dateFrom.isAfter(dateTo)) {
      throw new RequestParamInvalidException("Date invalid", "date", "invalid");
    }
    dateFrom = DatetimeUtil.atStartOfMonth(dateFrom);
    dateTo = DatetimeUtil.atEndOfMonth(dateTo);

    log.debug("Generate reservation month stats report with date: {} to {}", dateFrom, dateTo);

    final String fileName =
        String.format(
                reportNameTemplateProperties.getReservationMonthStatsReport(), dateFrom, dateTo)
            .concat(".pdf");
    return createFileByteResponse(
        reportService.getReservationMonthStatsReportPdf(dateFrom, dateTo), fileName);
  }

  @GetMapping("/suite_turnover_stats/pdf")
  public ResponseEntity<byte[]> getSuiteTurnOverStatsReport(
      final @RequestParam LocalDate dateFrom, final @RequestParam LocalDate dateTo) {
    if (dateFrom.isAfter(dateTo)) {
      throw new RequestParamInvalidException("Date invalid", "date", "invalid");
    }

    log.debug("Generate suite turnover stats report with date: {} to {}", dateFrom, dateTo);

    final String fileName =
        String.format(reportNameTemplateProperties.getSuiteTurnOverStatsReport(), dateFrom, dateTo)
            .concat(".pdf");
    return createFileByteResponse(
        reportService.getSuiteTurnOverStatsReportPdf(dateFrom, dateTo), fileName);
  }
}