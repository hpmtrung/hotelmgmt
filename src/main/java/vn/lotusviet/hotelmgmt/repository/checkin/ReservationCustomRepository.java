package vn.lotusviet.hotelmgmt.repository.checkin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.lotusviet.hotelmgmt.model.dto.reservation.ReservationDto;
import vn.lotusviet.hotelmgmt.model.dto.stats.ReservationMonthStatsRecord;

import java.time.LocalDate;
import java.util.List;

public interface ReservationCustomRepository {

  List<ReservationMonthStatsRecord> getReservationMonthStatsRecordsCurrentYear();

  List<ReservationMonthStatsRecord> getReservationMonthStatsRecordsFromDate(LocalDate dateFrom, LocalDate dateTo);

  Page<ReservationDto> findByDateRange(
      LocalDate dateFrom, LocalDate dateTo, String statuses, String personalId, Pageable pageable);
}