package uk.gov.cshr.report.domain.learnerrecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FromToParamsUserIds {
    protected LocalDate from;
    protected LocalDate to;
    private List<String> learnerIds;

}
