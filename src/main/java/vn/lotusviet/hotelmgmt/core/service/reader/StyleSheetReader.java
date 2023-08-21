package vn.lotusviet.hotelmgmt.core.service.reader;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;

import static org.apache.poi.ss.usermodel.CellType.*;

public abstract class StyleSheetReader <E extends Serializable> implements FileReader {

  private static final String FILE_INCORRECT_FORMAT_MSG = "File is in incorrect format.";

  private static final Map<Class<?>, CellType> TYPE_MAPPING = initTypeMapping();
  protected final Logger log = LoggerFactory.getLogger(getClass());

  private static Map<Class<?>, CellType> initTypeMapping() {
    Map<Class<?>, CellType> mapping = new HashMap<>();
    mapping.put(String.class, STRING);
    mapping.put(Long.class, NUMERIC);
    mapping.put(Integer.class, NUMERIC);
    mapping.put(Boolean.class, BOOLEAN);
    mapping.put(Date.class, NUMERIC);
    mapping.put(LocalDateTime.class, NUMERIC);
    return mapping;
  }

  @Override
  public final List<E> readFromFile(File file) {
    final List<E> records = new ArrayList<>();

    try (Workbook wb = WorkbookFactory.create(file)) {

      verifyValidWorkbook(wb);

      final Sheet sheet = getFirstSheet(wb);

      final int startRowIndex = getStartRowIndex(sheet);
      final int endRowIndex = sheet.getLastRowNum();

      verifyHasAvailableRows(startRowIndex, endRowIndex);

      log.debug("Read stylesheet from start row '{}' to end row '{}'", startRowIndex, endRowIndex);

      for (int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++) {
        Row row = sheet.getRow(rowIndex);

        if (isBlankRow(row)) {
          throw new FileReaderException(FILE_INCORRECT_FORMAT_MSG);
        }

        records.add(parseRow(row));
      }
    } catch (IOException e) {
      throw new FileReaderException("Stylesheet read failure", e);
    }

    return records;
  }

  private void verifyHasAvailableRows(int startRowIndex, int endRowIndex) {
    if (startRowIndex > endRowIndex) {
      throw new FileReaderException("File has empty rows.");
    }
  }

  protected final boolean isBlankRow(Row row) {
    if (row == null) return true;
    boolean isEmpty = row.getFirstCellNum() == -1;
    if (isEmpty) return true;
    for (Cell cell : row) {
      if (!isBlankCell(cell)) return false;
    }
    return true;
  }

  protected int getStartRowIndex(final Sheet sheet) {
    var headers = getIndexAndValueHeaderMapping();
    if (headers.isEmpty()) {
      throw new IllegalArgumentException("Index and value header mapping must not be empty");
    }
    for (Row row : sheet) {
      boolean match = true;
      for (var entry : headers.entrySet()) {
        int headerIndex = entry.getKey();
        String headerValue = entry.getValue();
        Cell cell = row.getCell(headerIndex);
        if (cell != null) {
          String cellValue = cell.getStringCellValue();
          match = cellValue.equalsIgnoreCase(headerValue);
        } else {
          match = false;
        }
        if (!match) break;
      }

      if (match) {
        return row.getRowNum() + 1;
      }
    }
    throw new FileReaderException(FILE_INCORRECT_FORMAT_MSG);
  }

  protected abstract Map<Integer, String> getIndexAndValueHeaderMapping();

  protected final Sheet getFirstSheet(final Workbook wb) {
    Sheet sheet = wb.getSheetAt(0);
    if (sheet == null) {
      throw new FileReaderException("No sheet found error.");
    }
    return sheet;
  }

  protected final boolean isBlankCell(Cell cell) {
    return cell == null || cell.getCellType().equals(BLANK);
  }

  protected final <T> T getCellValue(final Row row, CellScheme<T> scheme) {
    final Cell cell = row.getCell(scheme.getIndex());

    if (isBlankCell(cell)) {
      if (scheme.isRequired) {
        throw new CellReadException("Not blank", row.getRowNum(), scheme.getIndex());
      } else {
        return scheme.defaultValue;
      }
    }

    Class<T> expectedClass = scheme.expectedType;

    CellType expectedType = TYPE_MAPPING.get(expectedClass);

    boolean isTypeMismatch = !Objects.equals(cell.getCellType(), expectedType);
    if (isTypeMismatch) {
      if (scheme.strictType) {
        throw new CellReadException(
            String.format(
                "Cell type is mismatch, expected type '%s', current type '%s'.",
                expectedType, cell.getCellType().name()),
            row.getRowNum(),
            scheme.getIndex());
      } else {
        return scheme.defaultValue;
      }
    }

    Object rawValue;
    switch (expectedType) {
      case STRING:
        rawValue = cell.getRichStringCellValue().getString();
        break;
      case NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          rawValue = cell.getDateCellValue();
        } else {
          rawValue = cell.getNumericCellValue();
        }
        break;
      case BOOLEAN:
        rawValue = cell.getBooleanCellValue();
        break;
      case FORMULA:
        rawValue = cell.getCellFormula();
        break;
      default:
        rawValue = scheme.defaultValue;
    }

    T value = expectedClass.cast(rawValue);

    if (scheme.verifyFunction != null) {
      scheme.verifyFunction.accept(value, new CellScheme.MetaInfo(row.getRowNum(), scheme.index));
    }

    return value;
  }

  protected void verifyValidWorkbook(final Workbook wb) {}

  protected abstract E parseRow(final Row row);

  public static class CellScheme<T> {
    private final int index;
    private final Class<T> expectedType;
    private final T defaultValue;
    private final boolean isRequired;
    private final boolean strictType;
    private final BiConsumer<T, MetaInfo> verifyFunction;

    private CellScheme(
        int index,
        Class<T> expectedType,
        T defaultValue,
        boolean isRequired,
        boolean strictType,
        BiConsumer<T, MetaInfo> verifyFunction) {
      this.index = index;
      this.expectedType = expectedType;
      this.defaultValue = defaultValue;
      this.isRequired = isRequired;
      this.strictType = strictType;
      this.verifyFunction = verifyFunction;
    }

    public static <T> CellScheme<T> of(
        int index,
        Class<T> expectedType,
        T defaultValue,
        boolean isRequired,
        boolean strictType,
        BiConsumer<T, MetaInfo> verifyValue) {
      return new CellScheme<>(
          index, expectedType, defaultValue, isRequired, strictType, verifyValue);
    }

    public static <T> CellScheme<T> of(
        int index, Class<T> expectedType, T defaultValue, boolean isRequired, boolean strictType) {
      return new CellScheme<>(index, expectedType, defaultValue, isRequired, strictType, null);
    }

    public BiConsumer<T, MetaInfo> getVerifyFunction() {
      return verifyFunction;
    }

    public T getDefaultValue() {
      return defaultValue;
    }

    public boolean isStrictType() {
      return strictType;
    }

    public int getIndex() {
      return index;
    }

    public Class<T> getExpectedType() {
      return expectedType;
    }

    public boolean isRequired() {
      return isRequired;
    }

    public static class MetaInfo {
      private final int rowIndex;
      private final int cellIndex;

      public MetaInfo(int rowIndex, int cellIndex) {
        this.rowIndex = rowIndex;
        this.cellIndex = cellIndex;
      }

      public int getCellIndex() {
        return cellIndex;
      }

      public int getRowIndex() {
        return rowIndex;
      }
    }
  }

  public static final class CellReadException extends FileReaderException {

    private static final long serialVersionUID = -1686090395772310918L;

    private final int rowIndex;
    private final int cellIndex;

    public CellReadException(String reason, int rowIndex, int cellIndex) {
      super(
          String.format(
              "Cell read failure at row '%d' and index '%d' with reason: %s",
              rowIndex, cellIndex, reason));
      this.rowIndex = rowIndex;
      this.cellIndex = cellIndex;
    }

    public int getRowIndex() {
      return rowIndex;
    }

    public int getCellIndex() {
      return cellIndex;
    }
  }
}