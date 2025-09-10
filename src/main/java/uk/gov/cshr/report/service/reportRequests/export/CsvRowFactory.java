package uk.gov.cshr.report.service.reportRequests.export;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import uk.gov.cshr.report.domain.report.CsvData;
import uk.gov.cshr.report.domain.report.CustomColumnPositionStrategy;
import uk.gov.cshr.report.domain.report.ReportableData;

import java.util.List;

public abstract class CsvRowFactory<T extends ReportableData> {

    private final Class<T> csvClass;

    protected CsvRowFactory(Class<T> csvClass) {
        this.csvClass = csvClass;
    }

    public CsvData<T> getCsvData(List<T> inputs, ICsvConfig config) {
        ColumnPositionMappingStrategy<T> strategy = new CustomColumnPositionStrategy<>();
        strategy.setType(csvClass);
        strategy.setColumnMapping(config.getColumns());
        return new CsvData<>(strategy, inputs);
    }
}
