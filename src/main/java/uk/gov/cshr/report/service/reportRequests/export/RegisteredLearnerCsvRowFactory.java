package uk.gov.cshr.report.service.reportRequests.export;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.RegisteredLearner;

@Service
public class RegisteredLearnerCsvRowFactory extends CsvRowFactory<RegisteredLearner> {

    public RegisteredLearnerCsvRowFactory() {
        super(RegisteredLearner.class);
    }

}
