package vn.lotusviet.hotelmgmt.core.annotation.validation.address;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AddressValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AddressConstraint {

  String message() default "Invalid address";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}