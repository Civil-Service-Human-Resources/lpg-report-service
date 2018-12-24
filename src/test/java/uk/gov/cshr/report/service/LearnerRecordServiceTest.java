package uk.gov.cshr.report.service;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.util.UriBuilder;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.factory.UriBuilderFactory;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LearnerRecordServiceTest {

    private URI learnerRecordSummariesUrl;
    private URI learnerRecordEventsUri;
    private URI bookingUri;
    private String moduleRecordUri = "http://localhost/modules";
    private HttpService httpService = mock(HttpService.class);
    private LearnerRecordService learnerRecordService;
    private UriBuilderFactory uriBuilderFactory = mock(UriBuilderFactory.class);

    @Before
    public void setUp() throws Exception {
        learnerRecordSummariesUrl = new URI("http://localhost/learner-record-summaries");
        learnerRecordEventsUri = new URI("http://localhost/learner-record-events");
        bookingUri = new URI("http://localhost/bookings");

        learnerRecordService = new LearnerRecordService(httpService, uriBuilderFactory, learnerRecordSummariesUrl,
                learnerRecordEventsUri, bookingUri, moduleRecordUri);
    }

    @Test
    public void shouldReturnListOfBookings() {
        List<Booking> bookings = Lists.newArrayList(new Booking());
        when(httpService.getList(bookingUri, Booking.class)).thenReturn(bookings);

        assertEquals(bookings, learnerRecordService.getBookings());

        verify(httpService).getList(bookingUri, Booking.class);
    }

    @Test
    public void shouldReturnListOfModuleRecords() {
        LocalDateTime from = LocalDateTime.of(2018, 1, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2018, 1, 2, 0, 0);

        UriBuilder uriBuilder = mock(UriBuilder.class);
        URI uri = URI.create("http://locahost");

        when(uriBuilderFactory.builder(moduleRecordUri)).thenReturn(uriBuilder);
        when(uriBuilder.queryParam("from", from)).thenReturn(uriBuilder);
        when(uriBuilder.queryParam("to", to)).thenReturn(uriBuilder);
        when(uriBuilder.build(eq(new HashMap<>()))).thenReturn(uri);

        List<ModuleRecord> moduleRecords = Lists.newArrayList(new ModuleRecord());
        when(httpService.getList(uri, ModuleRecord.class)).thenReturn(moduleRecords);
        assertEquals(moduleRecords, learnerRecordService.getModules(from, to));

        verify(httpService).getList(uri, ModuleRecord.class);
    }
}