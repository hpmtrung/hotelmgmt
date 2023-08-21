package vn.lotusviet.hotelmgmt.service.report;

import org.apache.poi.ss.usermodel.CellType;
import vn.lotusviet.hotelmgmt.model.dto.report.TurnOverYearReportDto;
import vn.lotusviet.hotelmgmt.service.report.AbstractReport.ReportTable.Cell;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static vn.lotusviet.hotelmgmt.service.report.POIStyleGenerator.CustomCellStyle.*;

public class TurnOverReport extends AbstractReport {

  private final int year;
  private final String authorName;
  private final LocalDate createdAt;
  private final TurnOverYearReportDto data;

  public TurnOverReport(
      int year, String authorName, LocalDate createdAt, TurnOverYearReportDto data) {
    this.year = year;
    this.authorName = authorName;
    this.createdAt = createdAt;
    this.data = data;
  }

  @Override
  public String getMainTitle() {
    return "Báo cáo thống kê doanh thu";
  }

  @Override
  public Map<String, Object> getReportMetaInfo() {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("Năm:", this.year);
    map.put("Người lập:", authorName);
    map.put("Ngày lập:", dateFormatter.format(createdAt));
    return map;
  }

  @Override
  public ReportTable[] getReportTables() {
    final ReportTable turnOverMonthTable = new ReportTable();
    turnOverMonthTable.setHeaders(
        new Cell[] {
          new Cell<>().setValue("Tháng").setStyle(HEADER_TABLE).setMergeColumnNum((short) 2),
          new Cell<>().setValue("Doanh thu").setStyle(HEADER_TABLE).setMergeColumnNum((short) 2)
        });
    turnOverMonthTable.setRows(
        this.data.getTurnOverMonths().stream()
            .map(
                turnOverMonth ->
                    new Cell[] {
                      new Cell<>()
                          .setValue(turnOverMonth.getMonth())
                          .setStyle(CENTER_ALIGNED_WITH_BORDER)
                          .setType(CellType.NUMERIC)
                          .setMergeColumnNum((short) 2),
                      new Cell<>()
                          .setValue(turnOverMonth.getTurnOver())
                          .setStyle(RIGHT_ALIGNED_CURRECY_FORMAT_WITH_BORDER)
                          .setType(CellType.NUMERIC)
                          .setMergeColumnNum((short) 2),
                    })
            .toArray(Cell[][]::new));

    final ReportTable turnOverQuarterTable = new ReportTable();
    turnOverQuarterTable.setHeaders(
        new Cell[] {
          new Cell<>().setValue("Qúy").setStyle(HEADER_TABLE).setMergeColumnNum((short) 2),
          new Cell<>().setValue("Doanh thu").setStyle(HEADER_TABLE).setMergeColumnNum((short) 2)
        });
    turnOverQuarterTable.setRows(
        this.data.getTurnOverQuarters().stream()
            .map(
                turnOverQuarter ->
                    new Cell[] {
                      new Cell<>()
                          .setValue(turnOverQuarter.getQuarter())
                          .setStyle(CENTER_ALIGNED_WITH_BORDER)
                          .setType(CellType.NUMERIC)
                          .setMergeColumnNum((short) 2),
                      new Cell<>()
                          .setValue(turnOverQuarter.getTurnOver())
                          .setStyle(RIGHT_ALIGNED_CURRECY_FORMAT_WITH_BORDER)
                          .setType(CellType.NUMERIC)
                          .setMergeColumnNum((short) 2)
                    })
            .toArray(Cell[][]::new));

    return new ReportTable[] {turnOverMonthTable, turnOverQuarterTable};
  }
}