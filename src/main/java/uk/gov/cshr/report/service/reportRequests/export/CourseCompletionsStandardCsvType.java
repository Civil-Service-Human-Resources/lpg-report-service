package uk.gov.cshr.report.service.reportRequests.export;

public class CourseCompletionsStandardCsvType implements ICsvConfig {
    @Override
    public String[] getColumns() {
        return new String[]{"courseId",
                "courseTitle",
                "eventTimestamp",
                "organisationId",
                "organisationName",
                "professionId",
                "professionName",
                "gradeId",
                "gradeName"};
    }
}
