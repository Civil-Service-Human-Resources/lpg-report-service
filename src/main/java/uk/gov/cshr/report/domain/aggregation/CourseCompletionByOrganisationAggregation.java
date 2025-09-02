package uk.gov.cshr.report.domain.aggregation;

public interface CourseCompletionByOrganisationAggregation extends Aggregation {

    String getCourseId();
    Long getOrganisationId();

}
