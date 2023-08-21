package vn.lotusviet.hotelmgmt.config.datasource;

import net.ttddyy.dsproxy.listener.logging.QueryLogEntryCreator;

import javax.sql.DataSource;

public interface DatasourceProvider {

  DataSource getActualDatasource();

  QueryLogEntryCreator getQueryLogCreator();
}