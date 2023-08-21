package vn.lotusviet.hotelmgmt.exception;

import org.springframework.validation.FieldError;
import vn.lotusviet.hotelmgmt.core.exception.ConstraintViolationException;

import java.util.List;

public class SuiteIllegalUpdateException extends ConstraintViolationException {

  private static final long serialVersionUID = 3112029574296323289L;

  public SuiteIllegalUpdateException(List<FieldError> violations) {
    super("suite", violations);
  }

  public SuiteIllegalUpdateException(String fieldName, String errorKey) {
    super("suite", fieldName, errorKey);
  }
}