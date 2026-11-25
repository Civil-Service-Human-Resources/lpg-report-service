package uk.gov.cshr.report.domain.report;

import com.opencsv.CSVWriter;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.Getter;

import java.io.Writer;
import java.util.List;

@Getter
public class CsvData <T> {

    private final MappingStrategy<T> mappingStrategy;
    private final List<T> data;

    public CsvData(MappingStrategy<T> mappingStrategy, List<T> data) {
        this.mappingStrategy = mappingStrategy;
        this.data = data;
    }

    public void write(Writer writer) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        if (data == null || data.isEmpty()) {
            CSVWriter csvWriter = new CSVWriter(writer);
            csvWriter.writeNext(mappingStrategy.generateHeader(null), true);
            return;
        }
        StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                .withMappingStrategy(mappingStrategy)
                .build();
        beanToCsv.write(data);
    }
}
