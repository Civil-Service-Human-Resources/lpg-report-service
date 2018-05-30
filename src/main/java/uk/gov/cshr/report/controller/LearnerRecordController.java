package uk.gov.cshr.report.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.cshr.report.domain.LearnerRecordSummary;
import uk.gov.cshr.report.service.LearnerRecordService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Controller
@RequestMapping("/learner-record")
public class LearnerRecordController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LearnerRecordController.class);

    private LearnerRecordService learnerRecordService;

    @Autowired
    public LearnerRecordController(LearnerRecordService learnerRecordService) {
        checkArgument(learnerRecordService != null);
        this.learnerRecordService = learnerRecordService;
    }

    @GetMapping(produces = "text/csv")
    public void getLearnerRecordCSVReport(HttpServletResponse response)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        LOGGER.debug("Getting learner record CSV report");

        List<LearnerRecordSummary> summaries = learnerRecordService.listRecords();

        try (
                Writer writer = response.getWriter()
        ) {
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();

            beanToCsv.write(summaries);
        }
    }
}
