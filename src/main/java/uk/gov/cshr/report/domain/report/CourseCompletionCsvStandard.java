package uk.gov.cshr.report.domain.report;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCompletionCsvStandard{
    @CsvBindByPosition(position = 0)
    private String courseId;

    @CsvBindByPosition(position = 1)
    private String courseTitle;

    @CsvBindByPosition(position = 2)
    private LocalDateTime eventTimestamp;

    @CsvBindByPosition(position = 3)
    private Integer organisationId;

    @CsvBindByPosition(position = 4)
    private String organisationName;

    @CsvBindByPosition(position = 5)
    private Integer professionId;

    @CsvBindByPosition(position = 6)
    private String professionName;

    @CsvBindByPosition(position = 7)
    private Integer gradeId;

    @CsvBindByPosition(position = 8)
    private String gradeName;
}
