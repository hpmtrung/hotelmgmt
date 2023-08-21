package vn.lotusviet.hotelmgmt.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Profile("dev")
public class DevDatasourceProvider extends DatasourceProviderAdapter {

  @Override
  public DataSource getActualDatasource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(
        "jdbc:sqlserver://localhost:1433;database=HOTEL_MANAGEMENT;encrypt=true;trustServerCertificate=true;");
    dataSource.setUsername("sa");
    dataSource.setPassword("123");
    dataSource.setPoolName("Hikari");
    dataSource.setAutoCommit(false);
    dataSource.addDataSourceProperty("cachePrepStmts", true);
    dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
    dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
    dataSource.addDataSourceProperty("useServerPrepStmts", true);
    return dataSource;
  }
}