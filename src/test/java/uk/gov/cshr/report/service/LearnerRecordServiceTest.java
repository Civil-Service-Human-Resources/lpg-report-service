package uk.gov.cshr.report.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.cshr.report.client.learnerRecord.ILearnerRecordClient;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.BookingStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LearnerRecordServiceTest {
    private LearnerRecordService learnerRecordService;

    private ILearnerRecordClient learnerRecordClient;

    @BeforeEach
    public void setUp() {
        learnerRecordClient = mock(ILearnerRecordClient.class);

        learnerRecordService = new LearnerRecordService(learnerRecordClient);
    }

    @Test
    public void testLearnerRecordServiceGetBookingReturnsListOfBookingsReturnedFromClient() {
        LocalDate from = LocalDate.parse("2018-01-01");
        LocalDate to = LocalDate.parse("2018-01-31");

        Booking booking1 = new Booking();
        booking1.setLearner("Learner Name");
        booking1.setEventUid("Example event");
        booking1.setStatus(BookingStatus.CONFIRMED);

        Booking booking2 = new Booking();
        booking2.setLearner("Another Learner");
        booking2.setEventUid("Another event");
        booking2.setStatus(BookingStatus.CANCELLED);
        List<Booking> fakeBookingList = new ArrayList<>();
        fakeBookingList.add(booking1);
        fakeBookingList.add(booking2);

        when(learnerRecordClient.getBookings(from,to)).thenReturn(fakeBookingList);

        List<Booking> bookingListFromLearnerRecordService = learnerRecordService.getBookings(from, to);

        assertEquals(2, bookingListFromLearnerRecordService.size());
        assertEquals("Learner Name", bookingListFromLearnerRecordService.get(0).getLearner());
        assertEquals("Example event", bookingListFromLearnerRecordService.get(0).getEventUid());
        assertEquals(BookingStatus.CONFIRMED, bookingListFromLearnerRecordService.get(0).getStatus());
        assertEquals("Another Learner", bookingListFromLearnerRecordService.get(1).getLearner());
        assertEquals("Another event", bookingListFromLearnerRecordService.get(1).getEventUid());
        assertEquals(BookingStatus.CANCELLED, bookingListFromLearnerRecordService.get(1).getStatus());
    }

}
