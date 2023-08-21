package vn.lotusviet.hotelmgmt.core.annotation.validation.percent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PercentValidator implements ConstraintValidator<PercentConstaint, Integer> {

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    return value == null || (value >= 0 && value <= 100);
  }
}