package vn.lotusviet.hotelmgmt.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.model.dto.system.BusinessRuleKey;
import vn.lotusviet.hotelmgmt.repository.checkin.RentalDetailRepository;
import vn.lotusviet.hotelmgmt.repository.misc.BusinessRuleRepository;
import vn.lotusviet.hotelmgmt.repository.person.AccountRepository;

import java.util.Objects;

@Component
public class ScheduledJobManager {

  private static final Logger log = LoggerFactory.getLogger(ScheduledJobManager.class);

  private final RentalDetailRepository rentalDetailRepository;
  private final BusinessRuleRepository businessRuleRepository;
  private final CacheManager cacheManager;

  public ScheduledJobManager(
      RentalDetailRepository rentalDetailRepository,
      BusinessRuleRepository businessRuleRepository,
      CacheManager cacheManager) {
    this.rentalDetailRepository = rentalDetailRepository;
    this.businessRuleRepository = businessRuleRepository;
    this.cacheManager = cacheManager;
  }

  @Scheduled(cron = "0 0 0 * * ?")
  public void clearCachesForDBJobs() {
    Objects.requireNonNull(cacheManager.getCache(AccountRepository.CACHE_ACCOUNT_ENTITY_BY_ID))
        .clear();
  }

  /**
   * Fee overdued rental details
   *
   * <p>This is scheduled to get fired everyday, at 00:00 (am).
   */
  @Scheduled(cron = "0 0 0 * * ?")
  @Transactional
  public void updateFeeOfOverduedRentalDetails() {
    log.debug("Run task update fee of overdued rental details");
    businessRuleRepository
        .findById(BusinessRuleKey.RENTAL_DETAIL_OVERDUED_FEE_PERCENT.getId())
        .ifPresent(
            (rule) ->
                rentalDetailRepository.updateFeeOfOverduedRentalDetails(
                    Integer.parseInt(rule.getValue())));
  }
}