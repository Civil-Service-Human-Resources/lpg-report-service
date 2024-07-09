package uk.gov.cshr.report.domain.aggregation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public interface Aggregation {
    @JsonProperty("dateBin")
    LocalDateTime getdateBin();
    Integer getTotal();
}
