package uk.gov.cshr.report.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.config.partitionManager.PartitionManagerProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
@Configuration
@Component
@Data
public class AppConfig {

    private List<PartitionManagerProperties> partitionManager;
}
