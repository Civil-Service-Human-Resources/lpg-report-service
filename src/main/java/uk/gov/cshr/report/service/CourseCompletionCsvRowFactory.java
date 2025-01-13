package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.report.CourseCompletionCsv;
import uk.gov.cshr.report.domain.report.CustomColumnPositionStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseCompletionCsvRowFactory {

    public CsvData<CourseCompletionCsv> getCsvData(List<CourseCompletionEvent> courseCompletions) {
        CustomColumnPositionStrategy<CourseCompletionCsv> strategy = new CustomColumnPositionStrategy<>();
        strategy.setType(CourseCompletionCsv.class);
        List<CourseCompletionCsv> rows = getCsvRows(courseCompletions);
        return new CsvData<>(rows, strategy);
    }

    public CourseCompletionCsv getCsvRow(CourseCompletionEvent courseCompletion) {
        return new CourseCompletionCsv(
                courseCompletion.getUserEmail(),
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

    public List<CourseCompletionCsv> getCsvRows(List<CourseCompletionEvent> courseCompletions) {
        return courseCompletions.stream().map(this::getCsvRow).collect(Collectors.toList());
    }

}
