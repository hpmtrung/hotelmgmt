package vn.lotusviet.hotelmgmt.exception;

import org.springframework.validation.FieldError;
import vn.lotusviet.hotelmgmt.core.exception.ConstraintViolationException;

import java.util.List;

public class IllegalStateReservationException extends ConstraintViolationException {

  private static final String OBJECT_NAME = "reservation";

  private static final long serialVersionUID = -4112070451924570579L;

  public IllegalStateReservationException(List<FieldError> violations) {
    super(OBJECT_NAME, violations);
  }

  public IllegalStateReservationException(String fieldName, String errorKey) {
    super(OBJECT_NAME, fieldName, errorKey);
  }
}