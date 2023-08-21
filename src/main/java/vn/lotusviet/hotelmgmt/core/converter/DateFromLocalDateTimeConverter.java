package vn.lotusviet.hotelmgmt.core.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateFromLocalDateTimeConverter implements Converter<LocalDateTime, Date> {
  @Override
  public Date convert(LocalDateTime source) {
    return Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
  }
}