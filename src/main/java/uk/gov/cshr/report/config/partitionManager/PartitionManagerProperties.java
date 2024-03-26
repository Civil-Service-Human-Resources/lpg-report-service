package uk.gov.cshr.report.config.partitionManager;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PartitionManagerProperties {

    private String name;
    private boolean enabled;
    private String tableName;
    private List<String> indexColumns;
    private Integer partitionLookaheadInDays;
    private String cronSchedule;
}
