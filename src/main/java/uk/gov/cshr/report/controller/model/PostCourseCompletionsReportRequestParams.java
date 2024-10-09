package uk.gov.cshr.report.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    @NotNull
    private List<String> courseIds;

    private List<Integer> organisationIds;

    private List<Integer> professionIds;

    private List<Integer> gradeIds;

    @NotNull
    private String timezone;

}
