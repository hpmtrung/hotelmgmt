package vn.lotusviet.hotelmgmt.core.service.reader;

import org.springframework.http.HttpStatus;
import vn.lotusviet.hotelmgmt.core.exception.SimpleMessageException;

import java.io.File;
import java.util.List;

public interface FileReader {

  <T> List<T> readFromFile(File file);

  class FileReaderException extends SimpleMessageException {

    private static final long serialVersionUID = 5914523783666512443L;

    public FileReaderException(String message, Throwable cause) {
      super(HttpStatus.BAD_REQUEST, ErrorType.BAD_REQUEST_ERROR, "INVALID_FILE", message, cause);
    }

    public FileReaderException(Throwable cause) {
      this("Error on processing file.", cause);
    }

    public FileReaderException() {
      this(null, null);
    }

    public FileReaderException(String message) {
      this(message, null);
    }
  }
}