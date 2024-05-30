package uk.gov.cshr.report.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
public class MockClockConfig {

    @Bean
    @Primary
    public Clock getMockClock() {
        return Clock.fixed(Instant.parse("2024-01-01T10:00:00.000Z"), ZoneId.of("Europe/London"));
    }
}
