package vn.lotusviet.hotelmgmt.core.annotation.validation.taxcode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TaxCodeValidator implements ConstraintValidator<ValidTaxCode, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    return value == null || value.matches("^[\\w]{10}$");
  }
}