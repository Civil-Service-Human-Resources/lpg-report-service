package uk.gov.cshr.report.validation.validUrl;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CustomUrlValidatorTest {

    CustomUrlValidator customUrlValidator;

    @Test
    void isValid() {
        customUrlValidator = new CustomUrlValidator(List.of("https", "http"), null, null);
        assertTrue(customUrlValidator.isValid("https://test.com", mock(ConstraintValidatorContext.class)));
    }

    @Test
    void isNotValid() {
        customUrlValidator = new CustomUrlValidator(List.of("https"), null, null);
        assertFalse(customUrlValidator.isValid("http://test.com", mock(ConstraintValidatorContext.class)));
    }
}
