package vn.lotusviet.hotelmgmt.core.annotation.validation.name;

import vn.lotusviet.hotelmgmt.core.annotation.validation.AbstractValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static vn.lotusviet.hotelmgmt.core.annotation.validation.name.PersonNameConstraint.Type;

public class PersonNameValidator extends AbstractValidator
    implements ConstraintValidator<PersonNameConstraint, String> {

  private Type type;
  private boolean nullable;

  @Override
  public void initialize(PersonNameConstraint constraint) {
    this.type = constraint.type();
    this.nullable = constraint.nullable();
  }

  public boolean isValid(String value, ConstraintValidatorContext context) {
    return allowNullOrVerifyBySize(
        value, this.nullable, this.type.getMinLength(), this.type.getMaxLength());
  }
}