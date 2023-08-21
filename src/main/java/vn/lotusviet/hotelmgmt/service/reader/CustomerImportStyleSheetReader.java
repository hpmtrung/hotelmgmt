package vn.lotusviet.hotelmgmt.service.reader;

import vn.lotusviet.hotelmgmt.core.service.reader.FileReader;
import vn.lotusviet.hotelmgmt.model.entity.person.Customer;

import java.io.File;
import java.util.List;

public interface CustomerImportStyleSheetReader extends FileReader {

  List<Customer> readFromFile(File file);

}