package uk.gov.cshr.report.validation.timezone;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.DateTimeException;
import java.time.ZoneId;

@Slf4j
class ValidTimeZoneValidator
        implements ConstraintValidator<TimeZoneFormat, String> {

    @Override
    public void initialize(final TimeZoneFormat constraintAnnotation) {}

    @Override
    public boolean isValid(final String value,
                           final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        } else {
            try {
                ZoneId.of(value);
            } catch (DateTimeException e) {
                log.error(String.format("'%s' is not a valid timezone ID", value));
                return false;
            }
        }
        return true;
    }
}
