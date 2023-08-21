package vn.lotusviet.hotelmgmt.repository.ads.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.annotation.aop.LogAround;
import vn.lotusviet.hotelmgmt.core.repository.AbstractRepository;
import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;
import vn.lotusviet.hotelmgmt.model.entity.ads.Promotion;
import vn.lotusviet.hotelmgmt.model.entity.room.Suite;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteStyle;
import vn.lotusviet.hotelmgmt.model.entity.room.SuiteType;
import vn.lotusviet.hotelmgmt.repository.ads.PromotionCustomRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional(readOnly = true)
public class PromotionCustomRepositoryImpl extends AbstractRepository
    implements PromotionCustomRepository {

  @Override
  public Optional<PromotionDto> findCurrentPromotionOfSuite(int suiteId) {
    final LocalDate now = LocalDate.now();
    List<PromotionDto> promotions = findPromotionsOfSuiteByDate(suiteId, now, now);
    if (promotions.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(promotions.get(0));
  }

  @Override
  public List<PromotionDto> findPromotionsOfSuiteByDate(
      int suiteId, final LocalDate dateFrom, final LocalDate dateTo) {
    return (List<PromotionDto>)
        execProc(
                "Usp_LayKhuyenMaiCuaHangPhongTheoNgay",
                (rs, rowNum) -> getPromotionDtoFromRs(rs),
                Map.of("MA_HP", suiteId, "NGAY_BD", dateFrom, "NGAY_KT", dateTo))
            .getResultSet();
  }

  @Override
  public List<SuiteDto> findSuitesHasNoPromotionAtDateRange(LocalDate dateFrom, LocalDate dateTo) {
    return (List<SuiteDto>)
        execProc(
                "Usp_LayDsHangPhongKhongKMTheoNgay",
                (RowMapper<SuiteDto>) (rs, rowNum) -> getSuiteDtoFromRS(rs),
                Map.of("NGAY_BD", dateFrom, "NGAY_KT", dateTo))
            .getResultSet();
  }

  public final SuiteDto getSuiteDtoFromRS(ResultSet rs) throws SQLException {
    return new SuiteDto()
        .setId(rs.getInt(Suite.COL_MA_HANG_PHONG))
        .setStyleName(rs.getString(SuiteStyle.COL_TEN_KIEU_PHONG))
        .setTypeName(rs.getString(SuiteType.COL_TEN_LOAI_PHONG));
  }

  private PromotionDto getPromotionDtoFromRs(ResultSet rs) throws SQLException {
    return new PromotionDto()
        .setId(rs.getLong(Promotion.COL_MA_KHUYEN_MAI))
        .setCode(rs.getString(Promotion.COL_MA_CODE))
        .setStartAt(getValueAsLocalDate(rs, Promotion.COL_NGAY_BD))
        .setEndAt(getValueAsLocalDate(rs, Promotion.COL_NGAY_KT))
        .setDiscountPercent(rs.getInt(Suite.COL_PHAN_TRAM_KM));
  }
}