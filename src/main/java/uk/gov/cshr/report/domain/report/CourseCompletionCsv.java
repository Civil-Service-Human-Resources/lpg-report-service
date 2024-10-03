package uk.gov.cshr.report.domain.report;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCompletionCsv {

    @CsvBindByName(column = "user_email")
    @CsvBindByPosition(position = 1)
    private String userEmail;

    @CsvBindByName(column = "course_id")
    @CsvBindByPosition(position = 2)
    private String courseId;

    @CsvBindByName(column = "course_title")
    @CsvBindByPosition(position = 3)
    private String courseTitle;

    @CsvBindByName(column = "event_timestamp")
    @CsvBindByPosition(position = 4)
    private LocalDateTime eventTimestamp;

    @CsvBindByName(column = "organisation_id")
    @CsvBindByPosition(position = 5)
    private Integer organisationId;

    @CsvBindByName(column = "organisation_name")
    @CsvBindByPosition(position = 6)
    private String organisationName;

    @CsvBindByName(column = "profession_id")
    @CsvBindByPosition(position = 7)
    private Integer professionId;

    @CsvBindByName(column = "profession_name")
    @CsvBindByPosition(position = 8)
    private String professionName;

    @CsvBindByName(column = "grade_id")
    @CsvBindByPosition(position = 9)
    private Integer gradeId;

    @CsvBindByName(column = "grade_name")
    @CsvBindByPosition(position = 10)
    private String gradeName;
}
