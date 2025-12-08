package uk.gov.cshr.report.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import uk.gov.cshr.report.config.utils.RandomStringGenerator;
import uk.gov.cshr.report.config.utils.UUIDUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class MockUtilConfig {

    @Bean
    @Primary
    public Clock getMockClock() {
        return Clock.fixed(Instant.parse("2024-01-01T10:00:00.000Z"), ZoneId.of("Europe/London"));
    }

    @Bean
    @Primary
    RandomStringGenerator mockGenerator() {
        RandomStringGenerator mockGenerator = mock(RandomStringGenerator.class);
        when(mockGenerator.generateRandomString(any(Integer.class))).thenReturn("random-string");
        return mockGenerator;
    }

    @Bean
    @Primary
    UUIDUtils mockUUIDUtils() {
        UUIDUtils mockUUIDUtils = mock(UUIDUtils.class);
        when(mockUUIDUtils.generateUUID()).thenReturn("test-uid");
        return mockUUIDUtils;
    }
}
