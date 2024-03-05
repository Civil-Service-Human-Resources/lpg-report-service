package uk.gov.cshr.report.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.gov.cshr.report.domain.LearnerRecordEvent;
import uk.gov.cshr.report.domain.LearnerRecordSummary;
import uk.gov.cshr.report.service.LearnerRecordService;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Controller
@RequestMapping("/learner-record")
public class LearnerRecordController {

    private static final Logger LOG = LoggerFactory.getLogger(LearnerRecordController.class);

    private final LearnerRecordService learnerRecordService;

    public LearnerRecordController(LearnerRecordService learnerRecordService) {
        this.learnerRecordService = learnerRecordService;
    }

    @GetMapping(produces = "text/csv")
    @RequestMapping("/events")
    public void getLearnerRecordEventsCSVReport(HttpServletResponse response)
            throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

        LOG.debug("Getting learner record events CSV report");
        System.out.println("Inside /events");

        List<LearnerRecordEvent> events = learnerRecordService.listEvents();

        System.out.println("events: " + events.size());

        try (Writer writer = response.getWriter()) {
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .build();

            beanToCsv.write(events);
            response.flushBuffer();
        }
    }
}
