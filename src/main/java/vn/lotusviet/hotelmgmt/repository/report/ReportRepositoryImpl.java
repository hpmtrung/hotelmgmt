package vn.lotusviet.hotelmgmt.repository.report;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.repository.AbstractRepository;
import vn.lotusviet.hotelmgmt.model.dto.report.RoomOccupancyReportDto;
import vn.lotusviet.hotelmgmt.model.dto.report.RoomOccupancyReportDto.RoomOccupancyRecord;
import vn.lotusviet.hotelmgmt.model.dto.report.SuiteTurnOverStatsReportDto;
import vn.lotusviet.hotelmgmt.model.dto.report.TurnOverYearReportDto;
import vn.lotusviet.hotelmgmt.model.dto.stats.SuiteTurnOverStatsRecord;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteStyle;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@Transactional(readOnly = true)
public class ReportRepositoryImpl extends AbstractRepository implements ReportRepository {

  @Override
  @LogAround(jsonOutput = true)
  public TurnOverYearReportDto getTurnOverReportData(int year) {
    List<TurnOverYearReportDto> list =
        (List<TurnOverYearReportDto>)
            execProc(
                    "Usp_ThongKeDoanhThuTheoThang",
                    (RowMapper<TurnOverYearReportDto>)
                        (rs, rowNum) -> getTurnOverYearReportFromRs(rs),
                    Map.of("Year", year))
                .getResultSet();
    if (list.isEmpty()) {
      return TurnOverYearReportDto.createEmpty();
    } else {
      return list.get(0);
    }
  }

  @Override
  @LogAround(jsonOutput = true)
  public RoomOccupancyReportDto getRooomOccupancyReportData(LocalDate dateFrom, LocalDate dateTo) {
    RoomOccupancyReportDto dto = new RoomOccupancyReportDto();
    List<RoomOccupancyRecord> records =
        (List<RoomOccupancyRecord>)
            execProc(
                    "Usp_ThongKeCongSuatPhongTheoThang",
                    (rs, rowNum) -> getRoomOccupancyRecordFromRs(rs),
                    Map.of(
                        "NGAY_BD", dateFrom,
                        "NGAY_KT", dateTo))
                .getResultSet();
    return dto.setRecords(records);
  }

  @Override
  @LogAround(jsonOutput = true)
  public SuiteTurnOverStatsReportDto getSuiteTurnOverStatsReportDate(
      final LocalDate dateFrom, final LocalDate dateTo) {
    Objects.requireNonNull(dateFrom);
    Objects.requireNonNull(dateTo);
    SuiteTurnOverStatsReportDto dto = new SuiteTurnOverStatsReportDto();
    List<SuiteTurnOverStatsRecord> records =
        (List<SuiteTurnOverStatsRecord>)
            execProc(
                    "Usp_ThongKeTanSuatSuDungHPVaDoanhThu",
                    (rs, rowNum) -> getSuiteTurnOverStatsRecord(rs),
                    Map.of(
                        "NGAY_BD", dateFrom,
                        "NGAY_KT", dateTo))
                .getResultSet();
    return dto.setRecords(records);
  }

  private SuiteTurnOverStatsRecord getSuiteTurnOverStatsRecord(ResultSet rs) throws SQLException {
    return new SuiteTurnOverStatsRecord()
        .setSuiteTypeName(rs.getString(SuiteType.COL_TEN_LOAI_PHONG))
        .setSuiteStyleName(rs.getString(SuiteStyle.COL_TEN_KIEU_PHONG))
        .setOccupiedNum(rs.getInt("SO_PHONG_THUE"))
        .setTotal(rs.getInt("TONG_SO_PHONG"))
        .setTurnOver(rs.getLong("DOANH_THU"));
  }

  private RoomOccupancyRecord getRoomOccupancyRecordFromRs(ResultSet rs) throws SQLException {
    return new RoomOccupancyRecord()
        .setMonth(rs.getString("THANG"))
        .setSuiteTypeName(rs.getString(SuiteType.COL_TEN_LOAI_PHONG))
        .setSuiteStyleName(rs.getString(SuiteStyle.COL_TEN_KIEU_PHONG))
        .setOccupiedNum(rs.getInt("SO_PHONG_THUE"))
        .setTotal(rs.getInt("TONG_SO_PHONG"))
        .setPercent(rs.getFloat("PHAN_TRAM"));
  }

  private TurnOverYearReportDto getTurnOverYearReportFromRs(ResultSet rs) throws SQLException {
    final List<TurnOverYearReportDto.TurnOverMonth> months = new ArrayList<>();

    for (int i = 1; i <= 12; i++) {
      months.add(new TurnOverYearReportDto.TurnOverMonth().setMonth(i).setTurnOver(rs.getLong(i)));
    }
    return new TurnOverYearReportDto().setTurnOverMonths(months);
  }
}