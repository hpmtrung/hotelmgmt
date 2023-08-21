package vn.lotusviet.hotelmgmt.config;

import net.ttddyy.dsproxy.listener.logging.QueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import vn.lotusviet.hotelmgmt.config.datasource.DatasourceProvider;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableJpaRepositories({"vn.lotusviet.hotelmgmt.repository"})
@EnableMongoRepositories({"vn.lotusviet.hotelmgmt.repository"})
@EnableTransactionManagement
public class DatasourceConfig {

  private final ApplicationProperties.ProxyDatasource proxyDatasourceConfig;
  private final DatasourceProvider datasourceProvider;

  public DatasourceConfig(
      ApplicationProperties applicationProperties, DatasourceProvider datasourceProvider) {
    this.proxyDatasourceConfig = applicationProperties.getProxyDatasource();
    this.datasourceProvider = datasourceProvider;
  }

  @Bean
  public DataSource dataSource() {
    return ProxyDataSourceBuilder.create(datasourceProvider.getActualDatasource())
        .name(proxyDatasourceConfig.getName())
        .logQueryBySlf4j(proxyDatasourceConfig.getLogLevel())
        .logSlowQueryBySlf4j(
            proxyDatasourceConfig.getSlowQueryThresholdMiliseconds(), TimeUnit.MILLISECONDS)
        .countQuery() // collect query metrics
        .multiline()
        .listener(getQueryLoggingListener(datasourceProvider.getQueryLogCreator()))
        .build();
  }

  private SLF4JQueryLoggingListener getQueryLoggingListener(
      QueryLogEntryCreator queryLogEntryCreator) {
    SLF4JQueryLoggingListener listener = new SLF4JQueryLoggingListener();
    listener.setQueryLogEntryCreator(queryLogEntryCreator);
    return listener;
  }
}