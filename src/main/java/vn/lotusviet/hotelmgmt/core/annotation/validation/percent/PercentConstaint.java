package vn.lotusviet.hotelmgmt.core.annotation.validation.percent;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PercentValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PercentConstaint {
    String message() default "Invalid percent value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}