package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.report.CourseCompletionCsvStandard;
import uk.gov.cshr.report.domain.report.CustomColumnPositionStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseCompletionCsvRowStandardFactory {
    public CsvData<CourseCompletionCsvStandard> getCsvData(List<CourseCompletionEvent> courseCompletions) {
        CustomColumnPositionStrategy<CourseCompletionCsvStandard> strategy = new CustomColumnPositionStrategy<>();
        strategy.setType(CourseCompletionCsvStandard.class);
        List<CourseCompletionCsvStandard> rows = getCsvRows(courseCompletions);
        return new CsvData<>(rows, strategy);
    }

    public List<CourseCompletionCsvStandard> getCsvRows(List<CourseCompletionEvent> courseCompletions) {
        return courseCompletions.stream().map(this::getCsvRow).collect(Collectors.toList());
    }

    public CourseCompletionCsvStandard getCsvRow(CourseCompletionEvent courseCompletion) {
        return new CourseCompletionCsvStandard(
                courseCompletion.getCourseId(),
                courseCompletion.getCourseTitle(),
                courseCompletion.getEventTimestamp(),
                courseCompletion.getOrganisationId(),
                courseCompletion.getOrganisationName(),
                courseCompletion.getProfessionId(),
                courseCompletion.getProfessionName(),
                courseCompletion.getGradeId(),
                courseCompletion.getGradeName()
        );
    }
}
