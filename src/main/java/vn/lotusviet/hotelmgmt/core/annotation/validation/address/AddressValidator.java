package vn.lotusviet.hotelmgmt.core.annotation.validation.address;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressValidator implements ConstraintValidator<AddressConstraint, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return (value == null || value.trim().length() <= 200);
  }
}