package vn.lotusviet.hotelmgmt.config.datasource.querylog;

import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import org.hibernate.engine.jdbc.internal.FormatStyle;

public class HibernateQueryLogEntryCreator extends DefaultQueryLogEntryCreator {
  @Override
  protected String formatQuery(String query) {
    return FormatStyle.BASIC.getFormatter().format(query);
  }

}