package uk.gov.cshr.report.config.partitionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.config.AppConfig;
import uk.gov.cshr.report.service.PartitionManager;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(value = AppConfig.class)
public class PartitionManagerConfiguration implements SchedulingConfigurer {

    private final AppConfig appConfig;
    private final PartitionManager partitionManager;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        List<PartitionManagerProperties> partitionManagerPropertiesList = appConfig.getPartitionManager();
        partitionManagerPropertiesList.forEach(partitionManagerProperties -> {
            String name = partitionManagerProperties.getName();
            if (partitionManagerProperties.isEnabled()) {
                log.info("Creating '{}' partition manager", name);
                log.debug(partitionManagerProperties.toString());
                taskRegistrar.addCronTask(() -> partitionManager.createPartitions(partitionManagerProperties), partitionManagerProperties.getCronSchedule());
            } else {
                log.info("Partition manager '{}' is disabled; skipping initialisation", name);
            }
        });
    }

}
