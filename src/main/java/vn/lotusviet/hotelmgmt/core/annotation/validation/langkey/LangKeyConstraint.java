package vn.lotusviet.hotelmgmt.core.annotation.validation.langkey;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = LangKeyValidator.class)
public @interface LangKeyConstraint {
  String message() default "langKey.invalid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}