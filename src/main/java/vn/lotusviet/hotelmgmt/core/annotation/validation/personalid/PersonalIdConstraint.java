package vn.lotusviet.hotelmgmt.core.annotation.validation.personalid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PersonalIdValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PersonalIdConstraint {
  String message() default "personalId.invalid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}