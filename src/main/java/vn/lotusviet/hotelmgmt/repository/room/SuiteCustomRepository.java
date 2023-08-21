package vn.lotusviet.hotelmgmt.repository.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteReservationDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteSearchRequestDto;

import java.time.LocalDate;
import java.util.List;

public interface SuiteCustomRepository {
  List<SuiteDto> findTopNBestSaleSuites(Integer nSuite);

  Page<SuiteDto> findAvailableSuitesForReservation(SuiteSearchRequestDto filters, Pageable pageable);

  List<SuiteReservationDto> findAvailableSuitesForReservation(
      LocalDate checkInAt, LocalDate checkOutAt, SuiteSearchRequestDto filters);

  List<SuiteReservationDto> findAvailableSuitesForReservation(LocalDate checkInAt, LocalDate checkOutAt);
}