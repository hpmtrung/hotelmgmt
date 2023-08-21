package vn.lotusviet.hotelmgmt;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import vn.lotusviet.hotelmgmt.config.ApplicationProperties;
import vn.lotusviet.hotelmgmt.config.CacheConfig;
import vn.lotusviet.hotelmgmt.config.DatasourceConfig;
import vn.lotusviet.hotelmgmt.config.DatetimeFormatConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
// @DataJpaTest
// @AutoConfigureCache(cacheProvider = CacheType.JCACHE)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableConfigurationProperties(ApplicationProperties.class)
@SpringBootTest(
    classes = {
      CacheConfig.class,
      DatasourceConfig.class,
      DatetimeFormatConfig.class,
    })
@ActiveProfiles("dev")
public @interface RepositoryTest {}