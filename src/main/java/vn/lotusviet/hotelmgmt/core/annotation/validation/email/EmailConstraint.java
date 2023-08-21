package vn.lotusviet.hotelmgmt.core.annotation.validation.email;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface EmailConstraint {

  String message() default "Invalid email.";

  boolean nullable() default false;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}