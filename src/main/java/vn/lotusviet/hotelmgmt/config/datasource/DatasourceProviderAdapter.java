package vn.lotusviet.hotelmgmt.config.datasource;

import net.ttddyy.dsproxy.listener.logging.QueryLogEntryCreator;
import vn.lotusviet.hotelmgmt.config.datasource.querylog.HibernateQueryLogEntryCreator;

public abstract class DatasourceProviderAdapter implements DatasourceProvider{

  @Override
  public QueryLogEntryCreator getQueryLogCreator() {
    return new HibernateQueryLogEntryCreator();
  }

}