package uk.gov.cshr.report.repository.databaseManager;

import org.springframework.stereotype.Service;

@Service
public class DatabaseSqlHelper implements IDatabaseSqlHelper {


    @Override
    public String getCreateTimeBoundPartitionStatement(String partitionName, String parentTableName, String fromTimestamp, String toTimestamp) {
        return String.format("CREATE TABLE IF NOT EXISTS %s PARTITION OF %s FOR VALUES FROM ('%s') TO ('%s')",
                partitionName, parentTableName, fromTimestamp, toTimestamp);
    }

    @Override
    public String getCreateIndexStatement(String tableName, String indexColumn) {
        String indexName = String.format("idx_%s_%s",  indexColumn, tableName);
        return String.format("CREATE INDEX IF NOT EXISTS %s ON %s (%s)", indexName, tableName, indexColumn);
    }
}
