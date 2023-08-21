package vn.lotusviet.hotelmgmt.core.annotation.validation.email;

import vn.lotusviet.hotelmgmt.core.annotation.validation.AbstractValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator extends AbstractValidator
    implements ConstraintValidator<EmailConstraint, String> {

  private static final int MIN_LENGTH = 5;
  private static final int MAX_LENGTH = 100;
  private static final Pattern VALID_EMAIL_PATTERN = Pattern.compile("^[\\w-.]+@[\\w-]+.+[\\w-]{2,4}$");
  private boolean nullable;

  @Override
  public void initialize(EmailConstraint constraintAnnotation) {
    this.nullable = constraintAnnotation.nullable();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (nullable && value == null) return true;
    if (value != null) {
      int length = value.trim().length();
      boolean result = MIN_LENGTH <= length && length <= MAX_LENGTH;
      if (result) {
        return VALID_EMAIL_PATTERN.matcher(value).find();
      } else {
        return false;
      }
    }
    return false;
  }
}