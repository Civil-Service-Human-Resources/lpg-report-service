package uk.gov.cshr.report.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.cshr.report.domain.aggregation.CourseCompletionAggregation;
import uk.gov.cshr.report.integration.IntegrationTestBase;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CourseCompletionEventRepositoryTest extends IntegrationTestBase {

    @Autowired
    private CourseCompletionEventRepository courseCompletionEventRepository;

    private void assertAggregation(CourseCompletionAggregation aggregation, LocalDateTime expTimestamp,
                                   String expCourseId, Integer expCount) {
        assertNotNull(aggregation);
        assertEquals(expTimestamp, aggregation.getdateBin());
        assertEquals(expCourseId, aggregation.getCourseId());
        assertEquals(expCount, aggregation.getTotal());
    }

    @Test
    public void testGetCourseAggregations(){
        CourseCompletionAggregation[] completionsAggregationByCourse = courseCompletionEventRepository.getCompletionsAggregationByCourse("hour",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 23, 59), "UTC",
                List.of("c1", "c2"), List.of(1), null, null).toArray(CourseCompletionAggregation[]::new);
        assertEquals(5, completionsAggregationByCourse.length);
        assertAggregation(completionsAggregationByCourse[0], LocalDateTime.parse("2024-01-01T09:00:00"), "c1", 2);
        assertAggregation(completionsAggregationByCourse[1], LocalDateTime.parse("2024-01-01T10:00:00"), "c2", 1);
        assertAggregation(completionsAggregationByCourse[2], LocalDateTime.parse("2024-01-01T18:00:00"), "c2", 1);
        assertAggregation(completionsAggregationByCourse[3], LocalDateTime.parse("2024-01-01T19:00:00"), "c1", 1);
        assertAggregation(completionsAggregationByCourse[4], LocalDateTime.parse("2024-01-01T23:00:00"), "c1", 1);
    }

    @Test
    public void testGetCourseAggregationsWithTimezone(){
        CourseCompletionAggregation[] completionsAggregationByCourse = courseCompletionEventRepository.getCompletionsAggregationByCourse("hour",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 1, 23, 59), "+01",
                List.of("c1", "c2"), List.of(1), null, null).toArray(CourseCompletionAggregation[]::new);
        assertEquals(5, completionsAggregationByCourse.length);
        assertAggregation(completionsAggregationByCourse[0], LocalDateTime.parse("2024-01-01T10:00:00"), "c1", 2);
        assertAggregation(completionsAggregationByCourse[1], LocalDateTime.parse("2024-01-01T11:00:00"), "c2", 1);
        assertAggregation(completionsAggregationByCourse[2], LocalDateTime.parse("2024-01-01T19:00:00"), "c2", 1);
        assertAggregation(completionsAggregationByCourse[3], LocalDateTime.parse("2024-01-01T20:00:00"), "c1", 1);
        assertAggregation(completionsAggregationByCourse[4], LocalDateTime.parse("2024-01-02T00:00:00"), "c1", 1);
    }

    @Test
    public void testGetCourseAggregationsWithTimezoneAndDay(){
        CourseCompletionAggregation[] completionsAggregationByCourse = courseCompletionEventRepository.getCompletionsAggregationByCourse("day",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 2, 23, 59), "+01",
                List.of("c1"), List.of(1), null, null).toArray(CourseCompletionAggregation[]::new);
        assertEquals(2, completionsAggregationByCourse.length);
        assertAggregation(completionsAggregationByCourse[0], LocalDateTime.parse("2024-01-01T00:00:00"), "c1", 3);
        assertAggregation(completionsAggregationByCourse[1], LocalDateTime.parse("2024-01-02T00:00:00"), "c1", 1);
    }
}
