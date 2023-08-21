package vn.lotusviet.hotelmgmt.core.annotation.validation.password;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidtor.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {

  String message() default "Invalid password";

  boolean nullable() default false;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}