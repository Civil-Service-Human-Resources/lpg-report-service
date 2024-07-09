package uk.gov.cshr.report.validation.timezone;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidTimeZoneValidator.class)
public @interface TimeZoneFormat {
    String message() default "Invalid Timezone";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
