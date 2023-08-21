package vn.lotusviet.hotelmgmt.core.annotation.validation.password;

import vn.lotusviet.hotelmgmt.core.annotation.validation.AbstractValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidtor extends AbstractValidator
    implements ConstraintValidator<PasswordConstraint, String> {
  private static final int MIN_LENGTH = 5;
  private static final int MAX_LENGTH = 20;

  private boolean nullable;

  @Override
  public void initialize(PasswordConstraint constraint) {
    this.nullable = constraint.nullable();
  }

  public boolean isValid(String value, ConstraintValidatorContext context) {
    return allowNullOrVerifyBySize(value, this.nullable, MIN_LENGTH, MAX_LENGTH);
  }
}