package vn.lotusviet.hotelmgmt.repository.ads;

import vn.lotusviet.hotelmgmt.model.dto.ads.PromotionDto;
import vn.lotusviet.hotelmgmt.model.dto.room.SuiteDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PromotionCustomRepository {

  Optional<PromotionDto> findCurrentPromotionOfSuite(int suiteId);

  List<PromotionDto> findPromotionsOfSuiteByDate(int suiteId, LocalDate dateFrom, LocalDate dateTo);

  List<SuiteDto> findSuitesHasNoPromotionAtDateRange(LocalDate dateFrom, LocalDate dateTo);
}