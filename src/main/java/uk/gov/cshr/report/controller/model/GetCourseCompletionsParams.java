package uk.gov.cshr.report.controller.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCourseCompletionsParams {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ssZ")
    private ZonedDateTime startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ssZ")
    private ZonedDateTime endDate;

    @Size(min = 1, max = 30)
    @NotNull
    private List<String> courseIds;

    @Size(min = 1, max = 400)
    @NotNull
    private List<Integer> organisationIds;

    private List<Integer> professionIds;

    private List<Integer> gradeIds;

    private AggregationBinDelimiter binDelimiter = AggregationBinDelimiter.DAY;
}
