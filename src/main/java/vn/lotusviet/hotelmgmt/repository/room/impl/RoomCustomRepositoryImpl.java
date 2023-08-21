package vn.lotusviet.hotelmgmt.repository.room.impl;

import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.repository.AbstractRepository;
import vn.lotusviet.hotelmgmt.model.dto.rental.RentalDetailDto;
import vn.lotusviet.hotelmgmt.model.dto.room.RoomDto;
import vn.lotusviet.hotelmgmt.model.dto.room.RoomMapFilterOptionDTO;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;
import vn.lotusviet.hotelmgmt.model.dto.stats.RoomStatusStatsDto;
import vn.lotusviet.hotelmgmt.model.dto.stats.RoomStatusStatsRecord;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalDetail;
import vn.lotusviet.hotelmgmt.model.entity.room.*;
import vn.lotusviet.hotelmgmt.repository.room.RoomCustomRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Transactional(readOnly = true)
public class RoomCustomRepositoryImpl extends AbstractRepository implements RoomCustomRepository {

  @Override
  public RoomStatusStatsDto getRoomStatusStats() {
    List<RoomStatusStatsDto.Record> records =
        (List<RoomStatusStatsDto.Record>)
            execProc("Usp_LaySoPhongTheoTrangThai", (rs, rowNum) -> getRoomStatusStatsFromRS(rs))
                .getResultSet();

    return new RoomStatusStatsDto().setRecords(records);
  }

  @Override
  public List<RoomStatusStatsRecord> getRoomStatusStatsRecords() {
    return (List<RoomStatusStatsRecord>)
        execProc("Usp_LaySoPhongTheoTrangThai", (rs, rowNum) -> getRoomStatusStatsRecordFromRs(rs))
            .getResultSet();
  }

  @Override
  public List<RoomDto> getAllRoomsWithRentalDetail(final RoomMapFilterOptionDTO filterDto) {
    final HashMap<String, Object> map = new HashMap<>();
    map.put("TypeIds", filterDto.getTypeIds());
    map.put("StyleIds", filterDto.getStyleIds());
    map.put("HasPromotion", filterDto.getHasPromotion());
    map.put("PriceFrom", filterDto.getPriceFrom());
    map.put("PriceTo", filterDto.getPriceTo());
    map.put("Statuses", filterDto.getStatuses());
    return (List<RoomDto>)
        execProc("Usp_LaySoDoPhong", (rs, rowNum) -> getBasicRoomWithRentalDetailFromRS(rs), map)
            .getResultSet();
  }

  private RoomStatusStatsRecord getRoomStatusStatsRecordFromRs(ResultSet rs) throws SQLException {
    return new RoomStatusStatsRecord(
        RoomStatusCode.valueOf(rs.getString(RoomStatus.COL_CODE)), rs.getInt("SO_PHONG"));
  }

  private RoomStatusStatsDto.Record getRoomStatusStatsFromRS(ResultSet rs) throws SQLException {
    return new RoomStatusStatsDto.Record()
        .setStatusCode(RoomStatusCode.valueOf(rs.getString(RoomStatus.COL_CODE)))
        .setRoomNum(rs.getInt("SO_PHONG"));
  }

  private RoomDto getBasicRoomWithRentalDetailFromRS(final ResultSet rs) throws SQLException {
    RoomDto roomDto =
        new RoomDto()
            .setId(rs.getInt(Room.COL_MA_PHONG))
            .setName(rs.getString(Room.COL_TEN_PHONG))
            .setFloor(rs.getInt(Room.COL_VI_TRI_TANG))
            .setAvailable(rs.getBoolean(Room.COL_TRANG_THAI_HD))
            .setSuite(
                new SuiteDto()
                    .setId(rs.getInt(Suite.COL_MA_HANG_PHONG))
                    .setTypeName(rs.getString(SuiteType.COL_TEN_LOAI_PHONG))
                    .setStyleName(rs.getString(SuiteStyle.COL_TEN_KIEU_PHONG))
                    .setMaxOccupation(rs.getInt(SuiteStyle.COL_SO_NGUOI_LON))
                    .setOriginalPrice(rs.getInt(Suite.COL_GIA_HANG_PHONG)))
            .setStatusCode(RoomStatusCode.valueOf(rs.getString(RoomStatus.COL_CODE)));

    long rentalDetailId = rs.getLong(RentalDetail.COL_MA_CT_PHIEU_THUE);

    if (!rs.wasNull()) {
      roomDto.setRentalDetail(
          new RentalDetailDto()
              .setId(rentalDetailId)
              .setCheckInAt(getValueAsLocalDateTime(rs, RentalDetail.COL_NGAYGIO_CHECKIN))
              .setCheckOutAt(getValueAsLocalDateTime(rs, RentalDetail.COL_NGAYGIO_CHECKOUT)));
    }

    return roomDto;
  }
}