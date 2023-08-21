package vn.lotusviet.hotelmgmt.model.dto.report;

import vn.lotusviet.hotelmgmt.model.dto.stats.SuiteTurnOverStatsRecord;

import java.util.List;

public class SuiteTurnOverStatsReportDto {

  private List<SuiteTurnOverStatsRecord> records;

  public List<SuiteTurnOverStatsRecord> getRecords() {
    return records;
  }

  public SuiteTurnOverStatsReportDto setRecords(List<SuiteTurnOverStatsRecord> records) {
    this.records = records;
    return this;
  }

  @Override
  public String toString() {
    return "SuiteTurnOverStatsReportDto{" + "records=" + records + '}';
  }
}