package uk.gov.cshr.report.controller.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetCourseCompletionsParams {
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    private ZoneId timezone = ZoneId.of("UTC");

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
