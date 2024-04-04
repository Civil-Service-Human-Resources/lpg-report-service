package uk.gov.cshr.report.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;
import uk.gov.cshr.report.integration.IntegrationTestBase;
import uk.gov.cshr.report.repository.databaseManager.DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.readString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CourseCompletionEventRepositoryTest extends IntegrationTestBase {

    @Autowired
    private CourseCompletionEventRepository courseCompletionEventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private void assertAggregation(CourseCompletionAggregation aggregation, Instant expTimestamp,
                                   String expCourseId, Integer expCount) {
        assertEquals(expTimestamp, aggregation.getdateBin());
        assertEquals(expCourseId, aggregation.getCourseId());
        assertEquals(expCount, aggregation.getTotal());
    }

    @Test
    public void testGetCourseAggregations() throws IOException {
        File testFile = ResourceUtils.getFile(getClass().getResource("/data/sample-completions.json"));
        List<CourseCompletionEvent> completionEvents = objectMapper.readValue(testFile, new TypeReference<>() {});
        courseCompletionEventRepository.saveAll(completionEvents);

        CourseCompletionAggregation[] completionsAggregationByCourse = courseCompletionEventRepository.getCompletionsAggregationByCourse("hour",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 23, 0),
                List.of("course1", "course2"), List.of(1), null, null).toArray(new CourseCompletionAggregation[5]);
        assertAggregation(completionsAggregationByCourse[0], Instant.parse("2024-01-01T09:00:00.000Z"), "course1", 2);
        assertAggregation(completionsAggregationByCourse[1], Instant.parse("2024-01-01T10:00:00.000Z"), "course2", 1);
        assertAggregation(completionsAggregationByCourse[2], Instant.parse("2024-01-01T15:00:00.000Z"), "course1", 1);
        assertAggregation(completionsAggregationByCourse[3], Instant.parse("2024-01-01T18:00:00.000Z"), "course2", 1);
        assertAggregation(completionsAggregationByCourse[4], Instant.parse("2024-01-01T19:00:00.000Z"), "course1", 1);
    }
}
