package vn.lotusviet.hotelmgmt.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventType;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.lotusviet.hotelmgmt.model.entity.ads.Promotion;
import vn.lotusviet.hotelmgmt.model.entity.ads.PromotionProgram;
import vn.lotusviet.hotelmgmt.model.entity.paying.PaymentMethod;
import vn.lotusviet.hotelmgmt.model.entity.person.Authority;
import vn.lotusviet.hotelmgmt.model.entity.person.Department;
import vn.lotusviet.hotelmgmt.model.entity.person.Employee;
import vn.lotusviet.hotelmgmt.model.entity.rental.Invoice;
import vn.lotusviet.hotelmgmt.model.entity.rental.MergedInvoice;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalPayment;
import vn.lotusviet.hotelmgmt.model.entity.rental.RentalStatus;
import vn.lotusviet.hotelmgmt.model.entity.reservation.ReservationStatus;
import vn.lotusviet.hotelmgmt.model.entity.room.*;
import vn.lotusviet.hotelmgmt.model.entity.service.Service;
import vn.lotusviet.hotelmgmt.model.entity.service.ServiceType;
import vn.lotusviet.hotelmgmt.model.entity.tracking.ServicePriceHistory;
import vn.lotusviet.hotelmgmt.model.entity.tracking.SuitePriceHistory;
import vn.lotusviet.hotelmgmt.repository.checkin.ReservationStatusRepository;
import vn.lotusviet.hotelmgmt.repository.checkout.RentalStatusRepository;
import vn.lotusviet.hotelmgmt.repository.misc.BusinessRuleRepository;
import vn.lotusviet.hotelmgmt.repository.misc.PaymentMethodRepository;
import vn.lotusviet.hotelmgmt.repository.person.AccountRepository;
import vn.lotusviet.hotelmgmt.repository.person.AuthorityRepository;
import vn.lotusviet.hotelmgmt.repository.person.CustomerRepository;
import vn.lotusviet.hotelmgmt.repository.person.EmployeeRepository;
import vn.lotusviet.hotelmgmt.repository.room.SuiteStyleRepository;
import vn.lotusviet.hotelmgmt.security.ratelimit.APIBucketManager;
import vn.lotusviet.hotelmgmt.service.CommonService;
import vn.lotusviet.hotelmgmt.service.impl.PromotionServiceImpl;
import vn.lotusviet.hotelmgmt.service.impl.RoomServiceImpl;

import java.time.Duration;
import java.util.Set;

@Configuration(proxyBeanMethods = false)
@EnableCaching
public class CacheConfig {

  private static final CacheEventListenerConfigurationBuilder
      CACHE_EVENT_LISTENER_CONFIGURATION_BUILDER =
          CacheEventListenerConfigurationBuilder.newEventListenerConfiguration(
                  new CustomCacheEventListener(), Set.of(EventType.values()))
              .ordered()
              .synchronous();
  private final javax.cache.configuration.Configuration<Object, Object> defaultJcacheConfiguration;

  public CacheConfig(ApplicationProperties applicationProperties) {
    ApplicationProperties.Cache.Ehcache ehcache = applicationProperties.getCache().getEhcache();

    defaultJcacheConfiguration =
        createCacheConfig(
            Duration.ofSeconds(ehcache.getTimeToLiveSeconds()), ehcache.getMaxEntries());
  }

  @Bean
  public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(
      JCacheCacheManager cacheManager) {
    return properties ->
        properties.put(ConfigSettings.CACHE_MANAGER, cacheManager.getCacheManager());
  }

  @Bean
  public JCacheManagerCustomizer cacheManagerCustomizer() {
    return cm -> {
      createCache(cm, AccountRepository.CACHE_ACCOUNT_ENTITY_BY_ID);
      createCache(cm, AccountRepository.CACHE_ACCOUNT_ENTITY_BY_EMAIL);

      createCache(cm, Authority.CACHE_BY_ENTITY_ID);

      createCache(cm, CustomerRepository.CACHE_CUSTOMER_ENTITY_BY_PERSONAL_ID);
      createCache(cm, CustomerRepository.CACHE_CUSTOMER_ENTITY_BY_ACCOUNT_ID);

      createCache(cm, Department.CACHE);
      createCache(cm, Department.CACHE_EMPLOYEES);

      createCache(cm, Employee.CACHE);

      createCache(cm, Amenity.CACHE);

      createCache(cm, RoomStatus.CACHE);

      createCache(cm, Suite.CACHE);
      createCache(cm, Suite.CACHE_ROOMS);

      createCache(cm, SuiteType.CACHE);
      createCache(cm, SuiteType.CACHE_AMENITIES);
      createCache(cm, SuiteType.CACHE_SUITES);

      createCache(cm, SuiteStyle.CACHE);
      createCache(cm, SuiteStyle.CACHE_SUITES);

      createCache(cm, RentalPayment.CACHE_BY_ID);

      createCache(cm, Service.CACHE);

      createCache(cm, ServiceType.CACHE);
      createCache(cm, ServiceType.CACHE_SERVICES);

      createCache(cm, ReservationStatus.CACHE);
      createCache(cm, RentalStatus.CACHE);

      createCache(cm, PaymentMethod.CACHE);

      createCache(cm, Promotion.CACHE);
      createCache(cm, Promotion.CACHE_DETAILS);

      createCache(cm, MergedInvoice.CACHE_MERGED_INVOICE_BY_ID);
      createCache(cm, MergedInvoice.CACHE_MERGED_INVOICE_RENTALS);

      createCache(cm, Invoice.CACHE);
      createCache(cm, Invoice.CACHE_RENTAL_DETAILS);
      createCache(cm, Invoice.CACHE_SERVICE_USAGE_DETAILS);

      createCache(cm, PromotionProgram.CACHE);

      createCache(cm, ServicePriceHistory.CACHE);
      createCache(cm, SuitePriceHistory.CACHE);

      createCache(cm, APIBucketManager.RATE_LIMIT_CACHE_NAME);

      createCache(cm, EmployeeRepository.FIND_EMPLOYEE_BY_ACCOUNT_CACHE_NAME);
      createCache(cm, AuthorityRepository.FIND_AUTHORITY_BY_NAME);
      createCache(cm, ReservationStatusRepository.FIND_BY_CODE_CACHE_NAME);
      createCache(cm, PaymentMethodRepository.FIND_BY_CODE_CACHE_NAME);
      createCache(cm, SuiteStyleRepository.FIND_BY_NAME_CACHE_NAME);
      createCache(cm, SuiteStyleRepository.EXIST_BY_NAME_CACHE_NAME);
      createCache(cm, RentalStatusRepository.FIND_BY_CODE_CACHE_NAME);
      createCache(cm, BusinessRuleRepository.FIND_BY_KEY_CACHE_NAME);
      createCache(cm, CommonService.COMMON_DATA_APP_CACHE_NAME);
      createCache(cm, CustomerRepository.CACHE_CUSTOMER_ENTITY_BY_ACCOUNT_ID);
      createCache(cm, PromotionServiceImpl.CACHE_SUITE_PROMOTION);
      createCache(cm, RoomServiceImpl.CACHE_ROOM_FLOOR_NUMBERS);
    };
  }

  private javax.cache.configuration.Configuration<Object, Object> createCacheConfig(
      Duration duration, long maxEntries) {
    return Eh107Configuration.fromEhcacheCacheConfiguration(
        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class, Object.class, ResourcePoolsBuilder.heap(maxEntries))
            .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(duration))
            .withService(CACHE_EVENT_LISTENER_CONFIGURATION_BUILDER)
            .build());
  }

  private void createCache(javax.cache.CacheManager cm, String cacheName) {
    javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
    if (cache != null) {
      cache.clear();
    } else {
      cm.createCache(cacheName, defaultJcacheConfiguration);
    }
  }

  public static final class CustomCacheEventListener implements CacheEventListener<Object, Object> {

    private static final Logger log = LoggerFactory.getLogger(CustomCacheEventListener.class);

    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
      log.debug(
          "Cache event {} for item with key '{}'. Old value = '{}', New value = '{}'",
          cacheEvent.getType(),
          cacheEvent.getKey(),
          cacheEvent.getOldValue(),
          cacheEvent.getNewValue());
    }
  }
}