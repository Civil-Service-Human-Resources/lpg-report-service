package uk.gov.cshr.report.domain.aggregation;

import java.time.ZonedDateTime;

public interface Aggregation {
    ZonedDateTime getdateBin();
    Integer getTotal();
}
