package uk.gov.cshr.report.repository.databaseManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DatabaseManager {

    private final JdbcTemplate pgConnection;
    private final IDatabaseSqlHelper databaseSqlHelper;

    public void createTimePartition(String partitionName, String parentTableName, LocalDate from, Period period,
                                    List<String> columnsToIndex) {
        String fromFormat = from.format(DateTimeFormatter.ISO_DATE);
        String toFormat = from.plus(period).format(DateTimeFormatter.ISO_DATE);
        List<String> statements = new ArrayList<>() {{
            add(databaseSqlHelper.getCreateTimeBoundPartitionStatement(partitionName, parentTableName, fromFormat, toFormat));
            addAll(columnsToIndex.stream().map(columnName -> databaseSqlHelper.getCreateIndexStatement(parentTableName, columnName))
                    .toList());
        }};
        statements.forEach(statement -> {
            log.info(String.format("Executing SQL: '%s'", statement));
            pgConnection.execute(statement);
        });
    }

}
