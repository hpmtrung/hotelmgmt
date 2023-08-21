package vn.lotusviet.hotelmgmt.core.exception;

import org.springframework.validation.FieldError;

import java.util.List;

public class ConstraintViolationException extends RuntimeException {

  private static final long serialVersionUID = -1228407017357334177L;

  private final String objectName;
  private final List<FieldError> violations;

  public ConstraintViolationException(String objectName, List<FieldError> violations) {
    this.violations = violations;
    this.objectName = objectName;
  }

  public ConstraintViolationException(String objectName, String fieldName, String errorKey) {
    this.violations = List.of(new FieldError(objectName, fieldName, errorKey));
    this.objectName = objectName;
  }

  public List<FieldError> getViolations() {
    return violations;
  }

  public String getObjectName() {
    return objectName;
  }
}