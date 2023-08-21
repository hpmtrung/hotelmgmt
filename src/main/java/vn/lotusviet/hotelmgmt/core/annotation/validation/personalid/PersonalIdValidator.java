package vn.lotusviet.hotelmgmt.core.annotation.validation.personalid;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PersonalIdValidator implements ConstraintValidator<PersonalIdConstraint, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.hasText(value) && value.matches("^[\\d]{12}$");
  }
}