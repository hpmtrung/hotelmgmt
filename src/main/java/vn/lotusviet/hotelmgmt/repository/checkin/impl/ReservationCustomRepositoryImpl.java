package vn.lotusviet.hotelmgmt.repository.checkin.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.repository.AbstractRepository;
import vn.lotusviet.hotelmgmt.model.dto.person.CustomerDto;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationDto;
import vn.lotusviet.hotelmgmt.model.dto.stats.ReservationMonthStatsRecord;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.model.entity.reservation.Reservation;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatus;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatusCode;
import vn.lotusviet.hotelmgmt.repository.checkin.ReservationCustomRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
public class ReservationCustomRepositoryImpl extends AbstractRepository
    implements ReservationCustomRepository {

  private static final Comparator<ReservationMonthStatsRecord>
      RESERVATION_MONTH_STATS_RECORD_COMPARATOR =
          Comparator.comparing(ReservationMonthStatsRecord::getYear)
              .thenComparing(ReservationMonthStatsRecord::getMonth);

  private static ReservationMonthStatsRecord getReservationMonthStatsRecordFromRs(ResultSet rs)
      throws SQLException {
    return new ReservationMonthStatsRecord(
        rs.getInt("NAM"), rs.getInt("THANG"), rs.getInt("SO_PD_THUE"), rs.getInt("SO_PD_HUY"));
  }

  private static ReservationDto getDtoFromRS(ResultSet rs) throws SQLException {
    return new ReservationDto()
        .setId(rs.getLong(Reservation.COL_MA_PHIEU_DAT))
        .setCreatedAt(getValueAsInstant(rs, Reservation.COL_NGAYGIO_LAP))
        .setCheckInAt(getValueAsLocalDate(rs, Reservation.COL_NGAY_CHECKIN))
        .setCheckOutAt(getValueAsLocalDate(rs, Reservation.COL_NGAY_CHECKOUT))
        .setOwner(
            new CustomerDto()
                .setPersonalId(rs.getString(Customer.COL_CMND))
                .setLastName(rs.getString(Customer.COL_HO))
                .setFirstName(rs.getString(Customer.COL_TEN)))
        .setStatusCode(ReservationStatusCode.valueOf(rs.getString(ReservationStatus.COL_CODE)));
  }

  @Override
  public List<ReservationMonthStatsRecord> getReservationMonthStatsRecordsCurrentYear() {
    int currentYear = LocalDate.now().getYear();
    return getReservationMonthStatsRecordsFromDate(
        LocalDate.of(currentYear, 1, 1), LocalDate.of(currentYear, 12, 31));
  }

  @Override
  public List<ReservationMonthStatsRecord> getReservationMonthStatsRecordsFromDate(
      final LocalDate dateFrom, final LocalDate dateTo) {

    final List<ReservationMonthStatsRecord> records =
        (List<ReservationMonthStatsRecord>)
            execProc(
                    "Usp_LayThongKePhieuDatLuuVaHuyTheoThang",
                    (RowMapper<ReservationMonthStatsRecord>)
                        (rs, rowNum) -> getReservationMonthStatsRecordFromRs(rs),
                    Map.of("NGAY_BD", dateFrom, "NGAY_KT", dateTo))
                .getResultSet();
    Set<Integer> availableMonths =
        records.stream().map(ReservationMonthStatsRecord::getMonth).collect(Collectors.toSet());

    int monthFrom;
    int monthTo;

    for (int year = dateFrom.getYear(); year <= dateTo.getYear(); year++) {
      if (year == dateFrom.getYear()) {
        monthFrom = dateFrom.getMonthValue();
        monthTo = dateTo.getMonthValue();
      } else if (year == dateTo.getYear()) {
        monthFrom = 1;
        monthTo = dateTo.getMonthValue();
      } else {
        monthFrom = 1;
        monthTo = 12;
      }
      for (int month = monthFrom; month <= monthTo; month++) {
        if (!availableMonths.contains(month)) {
          records.add(new ReservationMonthStatsRecord(year, month, 0, 0));
        }
      }
    }
    records.sort(RESERVATION_MONTH_STATS_RECORD_COMPARATOR);

    return records;
  }

  @Override
  public Page<ReservationDto> findByDateRange(
      final LocalDate dateFrom,
      final LocalDate dateTo,
      final String statuses,
      final String personalId,
      Pageable pageable) {
    final ResultMap outputs =
        execProc(
            "Usp_LayDsPhieuDatTheoNgay",
            (RowMapper<ReservationDto>) (rs, rowNum) -> getDtoFromRS(rs),
            Map.of(
                "NGAY_BD",
                dateFrom,
                "NGAY_KT",
                dateTo,
                "CODES_TRANG_THAI_PD",
                statuses,
                "CMND",
                personalId,
                PAGE_INDEX,
                pageable.getPageNumber(),
                PAGE_SIZE,
                pageable.getPageSize()));
    return outputs.getAsPage(pageable);
  }
}