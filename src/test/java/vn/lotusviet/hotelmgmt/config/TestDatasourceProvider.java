package vn.lotusviet.hotelmgmt.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import vn.lotusviet.hotelmgmt.config.datasource.DatasourceProviderAdapter;

import javax.sql.DataSource;

@Component
@Profile("test")
public class TestDatasourceProvider extends DatasourceProviderAdapter {

  @Override
  public DataSource getActualDatasource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl("jdbc:h2:mem:dbtest;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
    dataSource.setAutoCommit(false);
    return dataSource;
  }
}