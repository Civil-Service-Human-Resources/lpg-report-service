package uk.gov.cshr.report.controller.model;


import lombok.Data;

@Data
public class DeleteUserResults {

    private final Integer deletedRegisteredLearners;
    private final Integer updatedCourseCompletions;

}
