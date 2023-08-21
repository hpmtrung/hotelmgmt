package vn.lotusviet.hotelmgmt.core.annotation.validation;

/**
 * Supported class for validator bean.
 */
public abstract class AbstractValidator {

  protected boolean allowNullOrVerifyBySize(String value, boolean nullable, int minLength, int maxLength) {
    if (nullable && value == null) return true;
    if (value != null) {
      int length = value.trim().length();
      return minLength <= length && length <= maxLength;
    }
    return false;
  }

}