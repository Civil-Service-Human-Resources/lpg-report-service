package uk.gov.cshr.report.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.report.CsvData;

import java.io.FileWriter;
import java.io.IOException;

@Service
public class CsvService {

    public <T> String createCsvFile(CsvData<T> data, String filename)  throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String csvFileName = String.format("%s.csv", filename);
        try (FileWriter fileWriter = new FileWriter(csvFileName)) {
            data.write(fileWriter);
        }
        return csvFileName;
    }

}
