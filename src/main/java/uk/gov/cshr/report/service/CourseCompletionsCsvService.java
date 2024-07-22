package uk.gov.cshr.report.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.CourseCompletionAggregationCsv;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CourseCompletionsCsvService {
    public void createCsvFile(List<CourseCompletionAggregationCsv> rows, String fileName) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        FileWriter fileWriter = new FileWriter(fileName);
        HeaderColumnNameMappingStrategy<CourseCompletionAggregationCsv> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(CourseCompletionAggregationCsv.class);

        StatefulBeanToCsv<CourseCompletionAggregationCsv> beanToCsv = new StatefulBeanToCsvBuilder<CourseCompletionAggregationCsv>(fileWriter)
                .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                .withMappingStrategy(strategy)
                .build();
        beanToCsv.write(rows);

        fileWriter.close();
    }
}
