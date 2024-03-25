package uk.gov.cshr.report.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.cshr.report.repository.databaseManager.DatabaseSqlHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class DatabaseSqlHelperTest {

    private final DatabaseSqlHelper databaseSqlHelper = new DatabaseSqlHelper();

    @Test
    public void testCreateTimeBoundPartitionStatement() {
        String statement = databaseSqlHelper.getCreateTimeBoundPartitionStatement("partitionName", "tableName",
                "from", "to");
        assertEquals("CREATE TABLE IF NOT EXISTS partitionName PARTITION OF tableName FOR VALUES FROM ('from') TO ('to')", statement);
    }

    @Test
    public void testCreateIndexStatement() {
        String statement = databaseSqlHelper.getCreateIndexStatement("tableName", "column");
        assertEquals("CREATE INDEX IF NOT EXISTS idx_column_tableName ON tableName (column)", statement);
    }

}
