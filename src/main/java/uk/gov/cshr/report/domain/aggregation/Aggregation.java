package uk.gov.cshr.report.domain.aggregation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public interface Aggregation {
    @JsonProperty("dateBin")
    ZonedDateTime getdateBin();
    Integer getTotal();
}
