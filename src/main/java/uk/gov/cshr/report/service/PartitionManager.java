package uk.gov.cshr.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.cshr.report.config.partitionManager.PartitionManagerProperties;
import uk.gov.cshr.report.repository.databaseManager.DatabaseManager;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
@Slf4j
public class PartitionManager {

    private final PartitionManagerProperties properties;
    private final Clock clock;
    private final DatabaseManager databaseManager;

    public void createPartitions() {
        Integer partitionLookaheadInDays = properties.getPartitionLookaheadInDays();
        String tableName = properties.getTableName();
        log.info(String.format("Creating partitions for today and the next %s days, if they don't already exist", partitionLookaheadInDays));
        LocalDate current = LocalDate.now(clock);
        Period period = Period.of(0, 0, 1);
        for (int i = 0; i <= partitionLookaheadInDays; i++) {
            log.info(String.format("Trying to create partition for table '%s' for the date '%s'", tableName, current));
            String partitionName = String.format("%s_%s_%s_%s", tableName, current.getYear(), current.getMonthValue(), current.getDayOfMonth());
            log.info(String.format("Partition name: %s", partitionName));
            databaseManager.createTimePartition(partitionName, tableName, current, period,
                    properties.getIndexColumns());
            current = current.plus(period);
        }
    }

}
