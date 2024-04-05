package uk.gov.cshr.report.controller.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCourseCompletionsParams {
    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotEmpty
    private List<String> courseIds;

    @NotEmpty
    private List<Integer> organisationIds;

    private List<Integer> professionIds;

    private List<Integer> gradeIds;

    private AggregationBinDelimiter binDelimiter = AggregationBinDelimiter.DAY;

    public ZonedDateTime getStartDate() {
        return startDate.atStartOfDay(ZoneId.systemDefault());
    }

    public ZonedDateTime getEndDate() {
        return endDate.atStartOfDay(ZoneId.systemDefault());
    }
}
