package uk.gov.cshr.report.integration;

import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.context.ActiveProfiles;
import uk.gov.cshr.report.repository.databaseManager.DatabaseManager;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("no-partition-manager")
public class DatabaseManagerIntegrationTest extends IntegrationTestBase {

    @Autowired
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(new PGSimpleDataSource());

    @Autowired
    private DatabaseManager databaseManager;

    @Test
    public void testCreateTimePartition() {
        jdbcTemplate.execute("CREATE TABLE test (id SERIAL, timestamp TIMESTAMP NOT NULL) PARTITION BY RANGE (timestamp)");
        databaseManager.createTimePartition(
                "test_partition", "test", LocalDate.of(2024, 1, 1),
                Period.of(0, 0, 1), List.of("id")
        );
        Integer result = (Integer) jdbcTemplate.query("""
                SELECT 1 as exists
                 FROM INFORMATION_SCHEMA.TABLES
                 WHERE TABLE_SCHEMA = 'public'
                 AND  TABLE_NAME = 'test_partition'""", (ResultSetExtractor<Object>) rs -> {
            if (rs.next()) {
                return rs.getInt("exists");
            } else {
                return null;
            }
        });
        assertEquals(1, result);
    }
}
