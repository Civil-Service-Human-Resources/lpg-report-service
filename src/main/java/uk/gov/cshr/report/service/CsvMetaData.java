package uk.gov.cshr.report.service;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CsvMetaData <T> {

    private final ColumnPositionMappingStrategy<T> positionMappingStrategy;
    private final List<T> rows;
}
