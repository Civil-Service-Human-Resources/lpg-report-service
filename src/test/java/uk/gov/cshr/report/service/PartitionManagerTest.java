package uk.gov.cshr.report.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.cshr.report.config.partitionManager.PartitionManagerProperties;
import uk.gov.cshr.report.repository.databaseManager.DatabaseManager;

import java.time.*;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PartitionManagerTest {

    private final Clock clock = Clock.fixed(Instant.parse("2024-01-01T10:00:00.000Z"), ZoneId.of("Europe/London"));

    private final DatabaseManager databaseManager = mock(DatabaseManager.class);

    private final PartitionManager partitionManager = new PartitionManager(clock, databaseManager);

    @Test
    public void testCreatePartitions() {
        List<String> cols = List.of("testIndex1", "testIndex2");
        PartitionManagerProperties props = new PartitionManagerProperties("test", true, "test_table",
                cols, 2, "* * * * * *", true);
        partitionManager.createPartitions(props);
        InOrder inOrder = inOrder(databaseManager);
        inOrder.verify(databaseManager).createTimePartition("test_table_2024_1_1", "test_table",
                LocalDate.of(2024, 1, 1), Period.of(0, 0, 1), cols);
        inOrder.verify(databaseManager).createTimePartition("test_table_2024_1_2", "test_table",
                LocalDate.of(2024, 1, 2), Period.of(0, 0, 1), cols);
        inOrder.verify(databaseManager).createTimePartition("test_table_2024_1_3", "test_table",
                LocalDate.of(2024, 1, 3), Period.of(0, 0, 1), cols);
    }

}
