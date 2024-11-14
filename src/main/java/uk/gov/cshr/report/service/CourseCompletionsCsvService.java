package uk.gov.cshr.report.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.report.CourseCompletionCsv;
import uk.gov.cshr.report.domain.report.CustomColumnPositionStrategy;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CourseCompletionsCsvService {
    public void createCsvFile(List<CourseCompletionCsv> rows, String fileName) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            CustomColumnPositionStrategy<CourseCompletionCsv> strategy = new CustomColumnPositionStrategy<>();
            strategy.setType(CourseCompletionCsv.class);
            StatefulBeanToCsv<CourseCompletionCsv> beanToCsv = new StatefulBeanToCsvBuilder<CourseCompletionCsv>(fileWriter)
                    .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .withMappingStrategy(strategy)
                    .build();
            beanToCsv.write(rows);
        }

    }
}
