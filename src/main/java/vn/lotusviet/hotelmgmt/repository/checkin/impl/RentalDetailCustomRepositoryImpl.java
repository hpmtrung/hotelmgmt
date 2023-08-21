package vn.lotusviet.hotelmgmt.repository.checkin.impl;

import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.repository.AbstractRepository;
import vn.lotusviet.hotelmgmt.model.dto.room.RoomDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;
import vn.lotusviet.hotelmgmt.model.entity.room.*;
import vn.lotusviet.hotelmgmt.repository.checkin.RentalDetailCustomRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional(readOnly = true)
public class RentalDetailCustomRepositoryImpl extends AbstractRepository
    implements RentalDetailCustomRepository {

  @Override
  @Transactional
  public void updateFeeOfOverduedRentalDetails(int feePercent) {
    execProc("Usp_CapNhatTienPhatCTPTQuaHan", Map.of("PHAN_TRAM", feePercent));
  }

  @Override
  public Optional<LocalDate> getMaximumCheckOutDate(long rentalDetailId, int dayThreshold) {
    final String dateAsString =
        execFunc(
            "Udf_LayNgayToiDaCheckOutCTPT",
            Map.of("MA_CTPT", rentalDetailId, "NGAY_THRESHOLD", dayThreshold),
            String.class);
    if (dateAsString.isBlank()) return Optional.empty();
    return Optional.of(LocalDate.parse(dateAsString));
  }

  @Override
  public List<RoomDto> getRentalDetailRoomChangeSuggestions(
      long rentalDetailId, boolean isSameSuite) {
    return (List<RoomDto>)
        execProc(
                "Usp_LayDsPhongThayThe",
                ((rs, rowNum) ->
                    new RoomDto()
                        .setId(rs.getInt(Room.COL_MA_PHONG))
                        .setName(rs.getString(Room.COL_TEN_PHONG))
                        .setFloor(rs.getInt(Room.COL_VI_TRI_TANG))
                        .setSuite(
                            new SuiteDto()
                                .setId(rs.getInt(Suite.COL_MA_HANG_PHONG))
                                .setTypeName(rs.getString(SuiteType.COL_TEN_LOAI_PHONG))
                                .setStyleName(rs.getString(SuiteStyle.COL_TEN_KIEU_PHONG))
                                .setMaxOccupation(rs.getInt(SuiteStyle.COL_SO_NGUOI_LON))
                                .setOriginalPrice(rs.getInt(Suite.COL_GIA_HANG_PHONG)))
                        .setStatusCode(RoomStatusCode.valueOf(rs.getString(RoomStatus.COL_CODE)))),
                Map.of("MA_CTPT", rentalDetailId, "CUNG_HANG_PHONG", isSameSuite))
            .getResultSet();
  }
}