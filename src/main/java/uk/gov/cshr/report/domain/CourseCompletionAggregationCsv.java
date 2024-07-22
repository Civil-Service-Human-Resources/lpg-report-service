package uk.gov.cshr.report.domain;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCompletionAggregationCsv {
    @CsvBindByName(column = "courseId")
    @CsvBindByPosition(position = 0)
    private String courseId;

    @CsvBindByName(column = "date")
    @CsvBindByPosition(position = 1)
    private ZonedDateTime date;

    @CsvBindByName(column = "total")
    @CsvBindByPosition(position = 2)
    private Integer total;
}
