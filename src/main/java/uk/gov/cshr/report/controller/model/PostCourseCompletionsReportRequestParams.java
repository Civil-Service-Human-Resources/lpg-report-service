package uk.gov.cshr.report.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCourseCompletionsReportRequestParams {
    @NotNull
    private String userId;

    @NotNull
    private String userEmail;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull
    private List<String> courseIds;

    private List<Integer> organisationIds;

    private List<Integer> professionIds;

    private List<Integer> gradeIds;

    private String requesterTimezone;
}
