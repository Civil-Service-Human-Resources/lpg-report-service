package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.cshr.report.domain.catalogue.Event;
import uk.gov.cshr.report.domain.catalogue.Module;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.domain.registry.CivilServant;
import uk.gov.cshr.report.factory.ReportRowFactory;
import uk.gov.cshr.report.reports.BookingReportRow;
import uk.gov.cshr.report.reports.ModuleReportRow;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        LocalDate from = LocalDate.parse("2018-01-01");
        LocalDate to = LocalDate.parse("2018-01-31");

        when(learnerRecordService.getBookings(from, to)).thenReturn(Arrays.asList(booking1, booking2));
        when(civilServantRegistryService.getCivilServantMap()).thenReturn(ImmutableMap.of(
                "learner1", civilServant1,
                "learner3", civilServant3
        ));
        when(learningCatalogueService.getEventMap()).thenReturn(ImmutableMap.of("event1", event));

        BookingReportRow reportRow = new BookingReportRow();
        when(reportRowFactory.createBookingReportRow(Optional.of(civilServant1), Optional.of(event), booking1)).thenReturn(reportRow);

        List<BookingReportRow> result = reportService.buildBookingReport(from, to);

        assertEquals(Collections.singletonList(reportRow), result);

        verify(reportRowFactory).createBookingReportRow(Optional.of(civilServant1), Optional.of(event), booking1);
        verifyNoMoreInteractions(reportRowFactory);
    }

    @Test
    public void shouldReturnModuleReport() {
        ModuleRecord moduleRecord1 = new ModuleRecord();
        moduleRecord1.setModuleId("module1");
        moduleRecord1.setLearner("learner1");

        ModuleRecord moduleRecord2 = new ModuleRecord();
        moduleRecord2.setModuleId("module2");
        moduleRecord2.setLearner("learner2");

        CivilServant civilServant1 = new CivilServant();
        civilServant1.setId("learner1");

        CivilServant civilServant2 = new CivilServant();
        civilServant2.setId("learner2");

        CivilServant civilServant3 = new CivilServant();
        civilServant3.setId("learner3");

        Module module = new Module();

        LocalDate from = LocalDate.of(2018, 1, 1);
        LocalDate to = LocalDate.of(2018, 1, 1);

        when(learnerRecordService.getModules(from ,to)).thenReturn(Arrays.asList(moduleRecord1, moduleRecord2));
        when(civilServantRegistryService.getCivilServantMap()).thenReturn(ImmutableMap.of(
                "learner1", civilServant1,
                "learner3", civilServant3
        ));
        when(learningCatalogueService.getModuleMap()).thenReturn(ImmutableMap.of("module1", module));

        ModuleReportRow reportRow = new ModuleReportRow();
        when(reportRowFactory.createModuleReportRow(civilServant1, module, moduleRecord1)).thenReturn(reportRow);

        List<ModuleReportRow> result = reportService.buildModuleReport(from, to);

        assertEquals(Collections.singletonList(reportRow), result);

        verify(reportRowFactory).createModuleReportRow(civilServant1, module, moduleRecord1);
        verifyNoMoreInteractions(reportRowFactory);
    }
}