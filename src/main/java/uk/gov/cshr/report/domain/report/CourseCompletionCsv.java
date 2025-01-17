package uk.gov.cshr.report.domain.report;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCompletionCsv {

    @CsvBindByPosition(position = 0)
    private String userEmail;

    @CsvBindByPosition(position = 1)
    private String courseId;

    @CsvBindByPosition(position = 2)
    private String courseTitle;

    @CsvBindByPosition(position = 3)
    private LocalDateTime eventTimestamp;

    @CsvBindByPosition(position = 4)
    private Integer organisationId;

    @CsvBindByPosition(position = 5)
    private String organisationName;

    @CsvBindByPosition(position = 6)
    private Integer professionId;

    @CsvBindByPosition(position = 7)
    private String professionName;

    @CsvBindByPosition(position = 8)
    private Integer gradeId;

    @CsvBindByPosition(position = 9)
    private String gradeName;
}
