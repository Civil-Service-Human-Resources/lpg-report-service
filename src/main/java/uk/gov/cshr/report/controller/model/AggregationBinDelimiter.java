package uk.gov.cshr.report.controller.model;

import lombok.Getter;

@Getter
public enum AggregationBinDelimiter {

    HOUR("hour"),
    DAY("day"),
    WEEK("week"),
    MONTH("month");

    private final String val;

    AggregationBinDelimiter(String val) {
        this.val = val;
    }

}
