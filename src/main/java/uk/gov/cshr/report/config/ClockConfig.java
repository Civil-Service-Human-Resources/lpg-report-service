package uk.gov.cshr.report.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfig {

    @Bean
    public Clock getClock() {
        return Clock.system(ZoneId.of("UTC"));
    }
}
