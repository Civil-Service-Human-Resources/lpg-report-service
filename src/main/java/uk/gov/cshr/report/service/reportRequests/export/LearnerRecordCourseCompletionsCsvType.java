package uk.gov.cshr.report.service.reportRequests.export;

public class LearnerRecordCourseCompletionsCsvType implements ICsvConfig {
    @Override
    public String[] getColumns() {
        return new String[]{
                "userId",
                "userEmail",
                "courseId",
                "courseTitle",
                "eventTimestamp",
                "organisationId",
                "organisationName",
                "professionId",
                "professionName",
                "gradeId",
                "gradeName"
        };
    }
}
