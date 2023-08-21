package vn.lotusviet.hotelmgmt.service.reader.impl;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vn.lotusviet.hotelmgmt.core.service.reader.StyleSheetReader;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;
import vn.lotusviet.hotelmgmt.service.reader.CustomerImportStyleSheetReader;

import java.util.HashMap;
import java.util.Map;

@Component
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class DefaultCustomerImportStyleSheetReader extends StyleSheetReader<Customer>
    implements CustomerImportStyleSheetReader {

  private static final CellScheme<String> PERSONAL_ID_SCHEME =
      CellScheme.of(
          0,
          String.class,
          null,
          true,
          true,
          (value, metaInfo) -> {
            boolean patternNotMatch = !value.matches("^\\d{12}$");
            if (patternNotMatch) {
              throw new CellReadException(
                  String.format("Personal id format is incorrect, value is '%s'.", value),
                  metaInfo.getRowIndex(),
                  metaInfo.getCellIndex());
            }
          });

  private static final CellScheme<String> LAST_NAME_SCHEME =
      CellScheme.of(1, String.class, null, true, true);

  private static final CellScheme<String> FIRST_NAME_SCHEME =
      CellScheme.of(2, String.class, null, true, true);

  private static final CellScheme<String> PHONE_SCHEME =
      CellScheme.of(
          3,
          String.class,
          null,
          true,
          true,
          (value, metaInfo) -> {
            boolean patternNotMatch = !value.matches("^\\d{10}$");
            if (patternNotMatch) {
              throw new CellReadException(
                  String.format("Phone number format is incorrect, value is :'%s'.", value),
                  metaInfo.getRowIndex(),
                  metaInfo.getCellIndex());
            }
          });

  private static final CellScheme<String> ADDRESS_SCHEME =
      CellScheme.of(4, String.class, "", false, true);

  @Override
  protected Map<Integer, String> getIndexAndValueHeaderMapping() {
    Map<Integer, String> map = new HashMap<>();
    map.put(0, "cmnd");
    map.put(1, "họ");
    map.put(2, "tên");
    map.put(3, "sđt");
    map.put(4, "địa chỉ");
    return map;
  }

  @Override
  protected Customer parseRow(Row row) {
    Customer customer = new Customer();
    customer.setPersonalId(getCellValue(row, PERSONAL_ID_SCHEME));
    customer.setLastName(getCellValue(row, LAST_NAME_SCHEME));
    customer.setFirstName(getCellValue(row, FIRST_NAME_SCHEME));
    customer.setPhone(getCellValue(row, PHONE_SCHEME));
    customer.setAddress(getCellValue(row, ADDRESS_SCHEME));
    return customer;
  }
}