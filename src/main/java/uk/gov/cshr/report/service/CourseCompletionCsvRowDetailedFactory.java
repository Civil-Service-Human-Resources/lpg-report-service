package uk.gov.cshr.report.service;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.CourseCompletionEvent;
import uk.gov.cshr.report.domain.report.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseCompletionCsvRowDetailedFactory {

    public CsvData<CourseCompletionCsvDetailed> getCsvData(List<CourseCompletionEvent> courseCompletions) {
        CustomColumnPositionStrategy<CourseCompletionCsvDetailed> strategy = new CustomColumnPositionStrategy<>();
        strategy.setType(CourseCompletionCsvDetailed.class);
        List<CourseCompletionCsvDetailed> rows = getCsvRows(courseCompletions);
        return new CsvData<>(rows, strategy);
    }

    public List<CourseCompletionCsvDetailed> getCsvRows(List<CourseCompletionEvent> courseCompletions) {
        return courseCompletions.stream().map(this::getCsvRow).collect(Collectors.toList());
    }

    public CourseCompletionCsvDetailed getCsvRow(CourseCompletionEvent courseCompletion) {
        return new CourseCompletionCsvDetailed(
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



}
