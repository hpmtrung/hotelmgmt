package vn.lotusviet.hotelmgmt.core.annotation.validation.name;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PersonNameValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface PersonNameConstraint {

  String message() default "Invalid person name";

  Type type();

  boolean nullable() default false;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  enum Type {
    FIRST_NAME(1, 100),
    LAST_NAME(1, 50);

    private final int minLength;
    private final int maxLength;

    Type(int minLength, int maxLength) {
      this.minLength = minLength;
      this.maxLength = maxLength;
    }

    public int getMaxLength() {
      return maxLength;
    }

    public int getMinLength() {
      return minLength;
    }
  }
}