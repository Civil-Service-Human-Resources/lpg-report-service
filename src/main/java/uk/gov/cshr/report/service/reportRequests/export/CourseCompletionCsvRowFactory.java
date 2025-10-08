package uk.gov.cshr.report.service.reportRequests.export;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.CourseCompletionEvent;

@Service
public class CourseCompletionCsvRowFactory extends CsvRowFactory<CourseCompletionEvent> {

    public CourseCompletionCsvRowFactory() {
        super(CourseCompletionEvent.class);
    }
}
