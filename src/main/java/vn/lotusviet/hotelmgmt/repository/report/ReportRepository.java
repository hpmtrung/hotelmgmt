package vn.lotusviet.hotelmgmt.repository.report;

import vn.lotusviet.hotelmgmt.model.dto.report.RoomOccupancyReportDto;
import vn.lotusviet.hotelmgmt.model.dto.report.SuiteTurnOverStatsReportDto;
import vn.lotusviet.hotelmgmt.model.dto.report.TurnOverYearReportDto;

import java.time.LocalDate;

public interface ReportRepository {

  TurnOverYearReportDto getTurnOverReportData(int year);

  RoomOccupancyReportDto getRooomOccupancyReportData(LocalDate dateFrom, LocalDate dateTo);

  SuiteTurnOverStatsReportDto getSuiteTurnOverStatsReportDate(LocalDate dateFrom, LocalDate dateTo);
}