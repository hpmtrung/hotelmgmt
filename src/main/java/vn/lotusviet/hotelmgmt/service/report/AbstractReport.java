package vn.lotusviet.hotelmgmt.service.report;

import org.apache.poi.ss.usermodel.CellType;
import vn.lotusviet.hotelmgmt.service.report.POIStyleGenerator.CustomCellStyle;

import java.time.format.DateTimeFormatter;
import java.util.Map;

public abstract class AbstractReport {

  protected final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public abstract String getMainTitle();

  public abstract Map<String, Object> getReportMetaInfo();

  public abstract ReportTable[] getReportTables();

  public static class ReportTable {
    private Cell<Object>[] headers;
    private Cell<Object>[][] rows;

    public Cell<Object>[] getHeaders() {
      return headers;
    }

    public void setHeaders(Cell<Object>[] headers) {
      this.headers = headers;
    }

    public Cell<Object>[][] getRows() {
      return rows;
    }

    public void setRows(Cell<Object>[][] rows) {
      this.rows = rows;
    }

    public static class Cell<T> {
      private T value;
      private CustomCellStyle style;
      private CellType type;
      private short mergeColumnNum;

      public short getMergeColumnNum() {
        return mergeColumnNum;
      }

      public Cell<T> setMergeColumnNum(short mergeColumnNum) {
        this.mergeColumnNum = mergeColumnNum;
        return this;
      }

      public CellType getType() {
        return type;
      }

      public Cell<T> setType(CellType type) {
        this.type = type;
        return this;
      }

      public T getValue() {
        return value;
      }

      public Cell<T> setValue(T value) {
        this.value = value;
        return this;
      }

      public CustomCellStyle getStyle() {
        return style;
      }

      public Cell<T> setStyle(CustomCellStyle style) {
        this.style = style;
        return this;
      }
    }
  }
}