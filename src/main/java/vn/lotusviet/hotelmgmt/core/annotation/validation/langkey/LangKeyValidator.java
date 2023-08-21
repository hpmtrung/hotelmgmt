package vn.lotusviet.hotelmgmt.core.annotation.validation.langkey;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class LangKeyValidator implements ConstraintValidator<LangKeyConstraint, String> {

  private static final List<String> SUPPORTED_LANG_KEYS = List.of("vi", "en");

  public boolean isValid(String value, ConstraintValidatorContext context) {
    return StringUtils.hasText(value) && SUPPORTED_LANG_KEYS.contains(value);
  }
}