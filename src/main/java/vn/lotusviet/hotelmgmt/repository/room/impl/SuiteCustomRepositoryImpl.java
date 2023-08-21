package vn.lotusviet.hotelmgmt.repository.room.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.repository.AbstractRepository;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteReservationDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteSearchRequestDto;
import vn.lotusviet.hotelmgmt.model.entity.ads.Promotion;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteStyle;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteType;
import vn.lotusviet.hotelmgmt.repository.room.SuiteCustomRepository;
import vn.lotusviet.hotelmgmt.service.storage.StorageService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional(readOnly = true)
public class SuiteCustomRepositoryImpl extends AbstractRepository implements SuiteCustomRepository {

  private final StorageService storageService;

  public SuiteCustomRepositoryImpl(StorageService storageService) {
    this.storageService = storageService;
  }

  @Override
  public List<SuiteDto> findTopNBestSaleSuites(Integer nSuite) {
    return (List<SuiteDto>)
        execProc(
                "Usp_LayDsHangPhongBanChay",
                (RowMapper<SuiteDto>) (rs, rowNum) -> getSuiteDtoFromResultSet(rs),
                Map.of("SoHangPhong", nSuite))
            .getResultSet();
  }

  @Override
  public Page<SuiteDto> findAvailableSuitesForReservation(SuiteSearchRequestDto filters, Pageable pageable) {
    final Sort.Order firstSort =
        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "price")).iterator().next();

    ResultMap outputs =
        execProc(
            "Usp_LayDsHangPhongTimKiem",
            (rs, rowNum) -> getSuiteDtoFromResultSet(rs),
            Map.of(
                "StyleIds",
                filters.getStyleIds(),
                "TypeIds",
                filters.getTypeIds(),
                "PriceFrom",
                filters.getPriceFrom(),
                "PriceTo",
                filters.getPriceTo(),
                "HasPromotion",
                filters.getHasPromotion(),
                "PageIndex",
                pageable.getPageNumber(),
                "PageSize",
                pageable.getPageSize(),
                "SortProperty",
                firstSort.getProperty(),
                "SortDirection",
                firstSort.getDirection()));

    List<SuiteDto> content = (List<SuiteDto>) outputs.getResultSet();
    int totalRows = outputs.getTotalRows();

    return new PageImpl<>(content, pageable, totalRows);
  }

  @Override
  public List<SuiteReservationDto> findAvailableSuitesForReservation(
      final LocalDate checkInAt, final LocalDate checkOutAt, final SuiteSearchRequestDto filters) {

    final HashMap<String, Object> params = new HashMap<>();
    params.put("NGAY_CHECKIN", checkInAt);
    params.put("NGAY_CHECKOUT", checkOutAt);
    params.put("MA_LOAI", filters.getTypeIds());
    params.put("MA_KIEU", filters.getStyleIds());
    params.put("CO_KHUYEN_MAI", filters.getHasPromotion());
    params.put("GIA_BD", filters.getPriceFrom());
    params.put("GIA_KT", filters.getPriceTo());
    params.put("MA_PHIEU_DAT_LOAI_TRU", filters.getReservationIdExcluded());
    params.put("MA_CT_PHIEU_THUE_LOAI_TRU", filters.getRentalDetailIdExcluded());

    return (List<SuiteReservationDto>)
        execProc(
                "Usp_LayDsHangPhongTrongTheoNgay_V2",
                (rs, rowNum) -> {
                  final SuiteReservationDto dto = getSuiteReservationDtoFromRs(rs);
                  dto.setEmptyRoomNum(rs.getInt(Suite.COL_SO_PHONG_TRONG));
                  return dto;
                },
                params)
            .getResultSet();
  }

  @Override
  public List<SuiteReservationDto> findAvailableSuitesForReservation(LocalDate checkInAt, LocalDate checkOutAt) {
    return (List<SuiteReservationDto>)
        execProc(
                "Usp_LayDsHangPhongTrongTheoNgay",
                (RowMapper<SuiteReservationDto>)
                    (rs, rowNum) -> {
                      SuiteReservationDto dto = getSuiteReservationDtoFromRs(rs);
                      dto.setEmptyRoomNum(rs.getInt(Suite.COL_SO_PHONG_TRONG));
                      return dto;
                    },
                Map.of("NGAY_CHECKIN", checkInAt, "NGAY_CHECKOUT", checkOutAt))
            .getResultSet();
  }

  private SuiteReservationDto getSuiteReservationDtoFromRs(ResultSet rs) throws SQLException {
    return new SuiteReservationDto()
        .setId(rs.getInt(Suite.COL_MA_HANG_PHONG))
        .setTypeName(rs.getString(SuiteType.COL_TEN_LOAI_PHONG))
        .setStyleName(rs.getString(SuiteStyle.COL_TEN_KIEU_PHONG))
        .setMaxOccupation(rs.getInt(SuiteStyle.COL_SO_NGUOI_LON))
        .setArea(rs.getInt(Suite.COL_DIEN_TICH_PHONG))
        .setOriginalPrice(rs.getInt(Suite.COL_GIA_HANG_PHONG))
        .setVat(rs.getInt(Suite.COL_VAT))
        .setImageURL(storageService.getFileURL(rs.getString(Suite.COL_ANH)))
        .setRentalNum(rs.getInt(Suite.COL_LUOT_MUA))
        .setAmenityIds(
            Stream.of(rs.getString(Suite.COL_TIEN_NGHI).split(", "))
                .map(Integer::parseInt)
                .collect(Collectors.toList()));
  }

  private SuiteDto getSuiteDtoFromResultSet(ResultSet rs) throws SQLException {
    final SuiteDto result =
        new SuiteDto()
            .setId(rs.getInt(Suite.COL_MA_HANG_PHONG))
            .setTypeName(rs.getString(SuiteType.COL_TEN_LOAI_PHONG))
            .setStyleName(rs.getString(SuiteStyle.COL_TEN_KIEU_PHONG))
            .setMaxOccupation(rs.getInt(SuiteStyle.COL_SO_NGUOI_LON))
            .setArea(rs.getInt(Suite.COL_DIEN_TICH_PHONG))
            .setOriginalPrice(rs.getInt(Suite.COL_GIA_HANG_PHONG))
            .setVat(rs.getInt(Suite.COL_VAT))
            .setImageURL(storageService.getFileURL(rs.getString(Suite.COL_ANH)))
            .setRentalNum(rs.getInt(Suite.COL_LUOT_MUA))
            .setAmenityIds(
                Stream.of(rs.getString(Suite.COL_TIEN_NGHI).split(", "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()));

    long promotionId = rs.getLong(Promotion.COL_MA_KHUYEN_MAI);

    if (!rs.wasNull()) {
      result.setPromotion(
          new PromotionDto()
              .setId(promotionId)
              .setDiscountPercent(rs.getInt(Suite.COL_PHAN_TRAM_KM))
              .setCode(rs.getString(Promotion.COL_MA_CODE))
              .setStartAt(getValueAsLocalDate(rs, Promotion.COL_NGAY_BD))
              .setEndAt(getValueAsLocalDate(rs, Promotion.COL_NGAY_KT)));
    }

    return result;
  }
}