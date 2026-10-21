package uk.gov.cshr.report.service.reportRequests.export;

import org.apache.commons.lang3.ArrayUtils;

public class CourseCompletionsDetailedCsvType extends CourseCompletionsStandardCsvType {

    @Override
    public String[] getColumns() {
        return ArrayUtils.insert(0, super.getColumns(), "userEmail", "userId");
    }
}
