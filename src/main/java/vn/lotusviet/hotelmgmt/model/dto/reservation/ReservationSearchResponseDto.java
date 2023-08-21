package vn.lotusviet.hotelmgmt.model.dto.reservation;

import vn.lotusviet.hotelmgmt.model.dto.room.SuiteReservationDto;

import java.util.ArrayList;
import java.util.List;

public class ReservationSearchResponseDto {

  private final List<List<Integer>> occupationMapSuiteIds = new ArrayList<>();
  private final List<SuiteReservationDto> reservableSuites = new ArrayList<>();

  public List<SuiteReservationDto> getReservableSuites() {
    return reservableSuites;
  }

  public List<List<Integer>> getOccupationMapSuiteIds() {
    return occupationMapSuiteIds;
  }

  @Override
  public String toString() {
    return "ReservationSearchResponseDto{"
        + "bookableSuites="
        + reservableSuites
        + ", bookableSuiteIds="
        + occupationMapSuiteIds
        + '}';
  }
}