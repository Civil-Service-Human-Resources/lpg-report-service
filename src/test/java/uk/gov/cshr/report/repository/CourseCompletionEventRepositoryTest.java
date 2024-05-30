package uk.gov.cshr.report.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;
import uk.gov.cshr.report.integration.IntegrationTestBase;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CourseCompletionEventRepositoryTest extends IntegrationTestBase {

    @Autowired
    private CourseCompletionEventRepository courseCompletionEventRepository;

    private void assertAggregation(CourseCompletionAggregation aggregation, ZonedDateTime expTimestamp,
                                   String expCourseId, Integer expCount) {
        assertNotNull(aggregation);
        assertEquals(expTimestamp, aggregation.getdateBin());
        assertEquals(expCourseId, aggregation.getCourseId());
        assertEquals(expCount, aggregation.getTotal());
    }

    @Test
    public void testGetCourseAggregations(){
        CourseCompletionAggregation[] completionsAggregationByCourse = courseCompletionEventRepository.getCompletionsAggregationByCourse("hour",
                ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 0, 0), ZoneId.systemDefault()),
                ZonedDateTime.of(LocalDateTime.of(2024, 1, 1, 23, 0), ZoneId.systemDefault()),
                List.of("c1", "c2"), List.of(1), null, null).toArray(new CourseCompletionAggregation[5]);
        assertAggregation(completionsAggregationByCourse[0], ZonedDateTime.parse("2024-01-01T09:00:00.000Z"), "c1", 2);
        assertAggregation(completionsAggregationByCourse[1], ZonedDateTime.parse("2024-01-01T10:00:00.000Z"), "c2", 1);
        assertAggregation(completionsAggregationByCourse[2], ZonedDateTime.parse("2024-01-01T15:00:00.000Z"), "c1", 1);
        assertAggregation(completionsAggregationByCourse[3], ZonedDateTime.parse("2024-01-01T18:00:00.000Z"), "c2", 1);
        assertAggregation(completionsAggregationByCourse[4], ZonedDateTime.parse("2024-01-01T19:00:00.000Z"), "c1", 1);
    }
}
