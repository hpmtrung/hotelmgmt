package vn.lotusviet.hotelmgmt.core.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateTimeFromDateConverter implements Converter<Date, LocalDateTime> {
  @Override
  public LocalDateTime convert(Date source) {
    return source.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }
}