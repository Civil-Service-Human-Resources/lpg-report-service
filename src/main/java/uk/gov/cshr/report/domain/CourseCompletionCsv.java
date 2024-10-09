package uk.gov.cshr.report.domain;

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
    @CsvBindByName(column = "event_id")
    @CsvBindByPosition(position = 1)
    private Long eventId;

    @CsvBindByName(column = "external_id")
    @CsvBindByPosition(position = 2)
    private String externalId;

    @CsvBindByName(column = "user_id")
    @CsvBindByPosition(position = 3)
    private String userId;

    @CsvBindByName(column = "user_email")
    @CsvBindByPosition(position = 4)
    private String userEmail;

    @CsvBindByName(column = "course_id")
    @CsvBindByPosition(position = 5)
    private String courseId;

    @CsvBindByName(column = "course_title")
    @CsvBindByPosition(position = 6)
    private String courseTitle;

    @CsvBindByName(column = "event_timestamp")
    @CsvBindByPosition(position = 7)
    private LocalDateTime eventTimestamp;

    @CsvBindByName(column = "organisation_id")
    @CsvBindByPosition(position = 8)
    private Integer organisationId;

    @CsvBindByName(column = "organisation_abbreviation")
    @CsvBindByPosition(position = 9)
    private String organisationAbbreviation;

    @CsvBindByName(column = "profession_id")
    @CsvBindByPosition(position = 10)
    private Integer professionId;

    @CsvBindByName(column = "profession_name")
    @CsvBindByPosition(position = 11)
    private String professionName;

    @CsvBindByName(column = "grade_id")
    @CsvBindByPosition(position = 12)
    private Integer gradeId;

    @CsvBindByName(column = "grade_code")
    @CsvBindByPosition(position = 13)
    private String gradeCode;
}
