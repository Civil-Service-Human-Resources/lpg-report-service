package uk.gov.cshr.report.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import uk.gov.cshr.report.service.util.StringUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Import({MockClockConfig.class})
public class TestConfig {

    @Primary
    @Bean
    public StringUtils stringUtils() {
        StringUtils mockStringUtils = mock(StringUtils.class);
        when(mockStringUtils.generateUid()).thenReturn("test-uid");
        when(mockStringUtils.generateRandomString(any())).thenReturn("random-string");
        return mockStringUtils;
    }
}
