
package uk.gov.cshr.report.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import uk.gov.cshr.report.controller.model.reportRequest.OrganisationalReportRequestParams;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostCourseCompletionsReportRequestParams extends OrganisationalReportRequestParams {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    private List<String> courseIds;

    private List<Integer> professionIds;

    private List<Integer> gradeIds;

    @NotNull
    private String timezone;

}
