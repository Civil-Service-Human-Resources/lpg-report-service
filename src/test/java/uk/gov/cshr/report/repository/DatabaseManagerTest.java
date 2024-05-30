package uk.gov.cshr.report.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.cshr.report.repository.databaseManager.DatabaseManager;
import uk.gov.cshr.report.repository.databaseManager.IDatabaseSqlHelper;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class DatabaseManagerTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private IDatabaseSqlHelper databaseSqlHelper;

    @InjectMocks
    private DatabaseManager databaseManager;

    @Test
    public void testPreparedStatementsAreExecuted() {
        String partitionName = "partition";
        String tableName = "table";
        LocalDate fromDate = LocalDate.of(2024, 1, 1);
        Period period = Period.of(1, 0, 0);
        List<String> columnsToIndex = List.of("column1", "column2");
        when(databaseSqlHelper.getCreateTimeBoundPartitionStatement(partitionName, tableName, "2024-01-01", "2025-01-01"))
                .thenReturn("partitionStatement");
        when(databaseSqlHelper.getCreateIndexStatement(partitionName, "column1"))
                .thenReturn("indexStatement");
        when(databaseSqlHelper.getCreateIndexStatement(partitionName, "column2"))
                .thenReturn("indexStatement2");
        databaseManager.createTimePartition(partitionName, tableName, fromDate, period, columnsToIndex);
        verify(jdbcTemplate, atMostOnce()).execute("partitionStatement");
        verify(jdbcTemplate, atMostOnce()).execute("indexStatement");
        verify(jdbcTemplate, atMostOnce()).execute("indexStatement2");
    }
}
