package uk.gov.cshr.report.service;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.UriBuilder;
import uk.gov.cshr.report.client.learnerRecord.ILearnerRecordClient;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.BookingStatus;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.factory.UriBuilderFactory;

import java.awt.print.Book;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LearnerRecordServiceTest {

    private URI learnerRecordSummariesUrl;
    private URI learnerRecordEventsUri;
    private String bookingUri;
    private String moduleRecordUri = "http://localhost/modules";
    private String moduleRecordsForLearnersUrl = "http://localhost/module-records-for-learners";
    private HttpService httpService = mock(HttpService.class);
    private LearnerRecordService learnerRecordService;
    private UriBuilderFactory uriBuilderFactory = mock(UriBuilderFactory.class);
    private String moduleRecordsForCourseIdsUrl = "http://localhost/module-records-for-course-ids";

    private ILearnerRecordClient learnerRecordClient;

    @BeforeEach
    public void setUp() throws Exception {
        learnerRecordSummariesUrl = new URI("http://localhost/learner-record-summaries");
        learnerRecordEventsUri = new URI("http://localhost/learner-record-events");
        bookingUri = "http://localhost/bookings";
        learnerRecordClient = mock(ILearnerRecordClient.class);

        learnerRecordService = new LearnerRecordService(httpService, uriBuilderFactory, learnerRecordSummariesUrl,
                learnerRecordEventsUri, bookingUri, moduleRecordUri, moduleRecordsForLearnersUrl, moduleRecordsForCourseIdsUrl, learnerRecordClient);
    }

    @Test
    public void testLearnerRecordServiceGetBookingReturnsListOfBookingsReturnedFromClient() {
        LocalDate from = LocalDate.parse("2018-01-01");
        LocalDate to = LocalDate.parse("2018-01-31");

        Booking booking1 = new Booking();
        booking1.setLearner("Learner Name");
        booking1.setEvent("Example event");
        booking1.setStatus(BookingStatus.CONFIRMED);

        Booking booking2 = new Booking();
        booking2.setLearner("Another Learner");
        booking2.setEvent("Another event");
        booking2.setStatus(BookingStatus.CANCELLED);
        List<Booking> fakeBookingList = new ArrayList<>();
        fakeBookingList.add(booking1);
        fakeBookingList.add(booking2);

        when(learnerRecordClient.getBookings(from,to)).thenReturn(fakeBookingList);

        List<Booking> bookingListFromLearnerRecordService = learnerRecordService.getBookings(from, to);

        assertEquals(2, bookingListFromLearnerRecordService.size());
        assertEquals("Learner Name", bookingListFromLearnerRecordService.get(0).getLearner());
        assertEquals("Example event", bookingListFromLearnerRecordService.get(0).getEvent());
        assertEquals(BookingStatus.CONFIRMED, bookingListFromLearnerRecordService.get(0).getStatus());
        assertEquals("Another Learner", bookingListFromLearnerRecordService.get(1).getLearner());
        assertEquals("Another event", bookingListFromLearnerRecordService.get(1).getEvent());
        assertEquals(BookingStatus.CANCELLED, bookingListFromLearnerRecordService.get(1).getStatus());
    }

    @Test
    public void shouldReturnListOfModuleRecords() {
        LocalDate from = LocalDate.of(2018, 1, 1);
        LocalDate to = LocalDate.of(2018, 1, 2);

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
