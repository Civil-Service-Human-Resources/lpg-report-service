package uk.gov.cshr.report.service;

import lombok.Data;
import uk.gov.cshr.report.domain.report.CustomColumnPositionStrategy;

import java.util.List;

@Data
public class CsvData <Row> {

    private final List<Row> rows;
    private final CustomColumnPositionStrategy<Row> strategy;

    public CsvData(List<Row> rows, CustomColumnPositionStrategy<Row> strategy) {
        this.rows = rows;
        this.strategy = strategy;
    }
}
