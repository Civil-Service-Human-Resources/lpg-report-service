package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.ReportRowFactory;
import uk.gov.cshr.report.reports.BookingReportRow;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {
    @Mock
    private LearnerRecordService learnerRecordService;

    @Mock
    private CivilServantRegistryService civilServantRegistryService;

    @Mock
    private LearningCatalogueService learningCatalogueService;

    @Mock
    private ReportRowFactory reportRowFactory;

    @InjectMocks
    private ReportService reportService;

    @Test
    public void shouldReturnBookingReport() {
        Booking booking1 = new Booking();
        booking1.setEvent("event1");
        booking1.setLearner("learner1");

        Booking booking2 = new Booking();
        booking2.setEvent("event2");
        booking2.setLearner("learner2");

        CivilServant civilServant1 = new CivilServant();
        civilServant1.setId("learner1");

        CivilServant civilServant2 = new CivilServant();
        civilServant2.setId("learner2");

        CivilServant civilServant3 = new CivilServant();
        civilServant3.setId("learner3");

        Event event = new Event();

        when(learnerRecordService.getBookings()).thenReturn(Arrays.asList(booking1, booking2));
        when(civilServantRegistryService.getCivilServantMap()).thenReturn(ImmutableMap.of(
                "learner1", civilServant1,
                "learner3", civilServant3
        ));
        when(learningCatalogueService.getEventMap()).thenReturn(ImmutableMap.of("event1", event));

        BookingReportRow reportRow = new BookingReportRow();
        when(reportRowFactory.createBookingReportRow(civilServant1, event, booking1)).thenReturn(reportRow);

        List<BookingReportRow> result = reportService.buildBookingReport();

        assertEquals(Collections.singletonList(reportRow), result);

        verify(reportRowFactory).createBookingReportRow(civilServant1, event, booking1);
        verifyNoMoreInteractions(reportRowFactory);
    }
}