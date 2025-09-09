package uk.gov.cshr.report.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MockUtilConfig.class})
public class TestConfig {


}
