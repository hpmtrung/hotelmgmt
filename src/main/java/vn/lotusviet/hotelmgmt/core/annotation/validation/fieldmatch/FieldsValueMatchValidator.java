package vn.lotusviet.hotelmgmt.core.annotation.validation.fieldmatch;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {
  private String field;
  private String fieldMatch;

  @Override
  public void initialize(FieldsValueMatch constraint) {
    this.field = constraint.field();
    this.fieldMatch = constraint.fieldMatch();
  }

  public boolean isValid(Object value, ConstraintValidatorContext context) {
    Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
    Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);

    if (fieldValue != null) {
      return fieldValue.equals(fieldMatchValue);
    } else {
      return fieldMatchValue == null;
    }
  }
}