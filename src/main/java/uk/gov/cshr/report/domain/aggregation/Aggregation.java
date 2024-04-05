package uk.gov.cshr.report.domain.aggregation;

import java.time.Instant;

public interface Aggregation {
    Instant getdateBin();
    Integer getTotal();
}
