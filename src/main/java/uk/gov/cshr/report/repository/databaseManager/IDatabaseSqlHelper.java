package uk.gov.cshr.report.repository.databaseManager;

public interface IDatabaseSqlHelper {

    String getCreateTimeBoundPartitionStatement(String partitionName, String parentTableName,
                                                           String fromTimestamp, String toTimestamp);
    String getCreateIndexStatement(String tableName, String indexColumn);
}
