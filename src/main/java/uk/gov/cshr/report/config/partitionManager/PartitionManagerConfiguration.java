package uk.gov.cshr.report.config.partitionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.config.AppConfig;
import uk.gov.cshr.report.repository.databaseManager.DatabaseManager;
import uk.gov.cshr.report.service.PartitionManager;

import java.time.Clock;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(value = AppConfig.class)
public class PartitionManagerConfiguration implements SchedulingConfigurer {

    private final AppConfig appConfig;
    private final Clock clock;
    private final DatabaseManager databaseManager;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        List<PartitionManagerProperties> partitionManagerPropertiesList = appConfig.getPartitionManager();
        partitionManagerPropertiesList.forEach(partitionManagerProperties -> {
            String name = partitionManagerProperties.getName();
            if (partitionManagerProperties.isEnabled()) {
                log.info("Creating '{}' partition manager", name);
                log.debug(partitionManagerProperties.toString());
                PartitionManager partitionManager = new PartitionManager(partitionManagerProperties, clock, databaseManager);
                taskRegistrar.addCronTask(partitionManager::createPartitions, partitionManagerProperties.getCronSchedule());
            } else {
                log.info("Partition manager '{}' is disabled; skipping initialisation", name);
            }
        });
    }

}
