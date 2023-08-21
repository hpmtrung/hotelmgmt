package vn.lotusviet.hotelmgmt.core.annotation.validation.phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<PhoneConstraint, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || value.matches("^[\\d]{10}$");
  }
}