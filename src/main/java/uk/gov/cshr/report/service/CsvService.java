package uk.gov.cshr.report.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

@Service
public class CsvService<T> {

    public String createCsvFile(CsvData<T> data, String fileName) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String csvFileName = String.format("%s.csv", fileName);
        try (FileWriter fileWriter = new FileWriter(csvFileName)) {
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(fileWriter)
                    .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .withMappingStrategy(data.getStrategy())
                    .build();
            beanToCsv.write(data.getRows());
        }
        return csvFileName;
    }
}
