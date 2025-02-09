package uk.gov.cshr.report.domain.report;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class CustomColumnPositionStrategy<T> extends ColumnPositionMappingStrategy<T> {

    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
        super.generateHeader(bean);
        return super.getColumnMapping();
    }
}
