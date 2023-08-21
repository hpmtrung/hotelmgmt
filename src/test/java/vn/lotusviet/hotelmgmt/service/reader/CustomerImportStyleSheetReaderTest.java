package vn.lotusviet.hotelmgmt.service.reader;

import org.junit.jupiter.api.Test;
import vn.lotusviet.hotelmgmt.TestUtil;
import vn.lotusviet.hotelmgmt.core.service.reader.FileReader.FileReaderException;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.service.reader.impl.DefaultCustomerImportStyleSheetReader;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class CustomerImportStyleSheetReaderTest {

  private static final String INCORRECT_FORMAT_TEST_MSG = "File is in incorrect format.";
  private final DefaultCustomerImportStyleSheetReader reader =
      new DefaultCustomerImportStyleSheetReader();

  @Test
  void whenReadCorrectFormatXLS_thenGetCorrectResult() {
    var records = getResultListFromSheet("test-correct.xls");
    assertThat(records).hasSize(10);
  }

  @Test
  void whenReadCorrectFormatXLSX_thenGetCorrectResult() {
    var records = getResultListFromSheet("test-correct.xlsx");
    assertThat(records).hasSize(10);
  }

  @Test
  void whenNoRowFound_thenGetException() {
    verifyGetFileReadExceptionWithMessage("test-no-rows.xlsx", "File has empty rows.");
  }

  @Test
  void whenHasEmptyRowFound_thenGetException() {
    verifyGetFileReadExceptionWithMessage("test-empty-row-error.xlsx", INCORRECT_FORMAT_TEST_MSG);
  }

  @Test
  void whenHasBlankRowFound_thenGetException() {
    verifyGetFileReadExceptionWithMessage("test-blank-row-error.xlsx", INCORRECT_FORMAT_TEST_MSG);
  }

  @Test
  void whenHasDeletedColumnFound_thenGetException() {
    verifyGetFileReadExceptionWithMessage(
        "test-deleted-column-error.xlsx", INCORRECT_FORMAT_TEST_MSG);
  }

  @Test
  void whenHasRowBreakError_thenGetException() {
    verifyGetFileReadExceptionWithMessage("test-row-break-error.xlsx", INCORRECT_FORMAT_TEST_MSG);
  }

  @Test
  void whenHasCellTypeMisMatch_thenGetException() {
    var exception =
        assertThrows(
            FileReaderException.class, () -> getResultListFromSheet("test-cell-type-error.xlsx"));

    String message =
        String.format(
            "Cell read failure at row '%d' and index '%d' with reason: Cell type is mismatch, expected type '%s', current type '%s'.",
            7, 3, "STRING", "NUMERIC");
    assertThat(exception.getMessage()).isEqualTo(message);
  }

  @Test
  void whenHasCellPatternIncorrect_thenGetException() {
    var exception =
        assertThrows(
            FileReaderException.class,
            () -> getResultListFromSheet("test-cell-pattern-error.xlsx"));

    String message =
        String.format(
            "Cell read failure at row '%d' and index '%d' with reason: Personal id format is incorrect, value is '00330ab41263'.",
            10, 0);
    assertThat(exception.getMessage()).isEqualTo(message);
  }

  private void verifyGetFileReadExceptionWithMessage(String filename, String message) {
    var exception = assertThrows(FileReaderException.class, () -> getResultListFromSheet(filename));

    assertThat(exception.getMessage()).isEqualTo(message);
  }

  private List<Customer> getResultListFromSheet(String filename) {
    File file;
    List<Customer> records;
    try {
      file = TestUtil.getResourceAsFile("sheet/customer-import/" + filename);
      records = reader.readFromFile(file);
      return records;
    } catch (URISyntaxException e) {
      fail();
      return Collections.emptyList();
    }
  }
}