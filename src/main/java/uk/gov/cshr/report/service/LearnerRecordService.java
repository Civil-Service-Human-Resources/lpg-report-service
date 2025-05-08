package uk.gov.cshr.report.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.client.learnerRecord.ILearnerRecordClient;
import uk.gov.cshr.report.domain.LearnerRecordEvent;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;

import java.time.LocalDate;
import java.util.List;

@Service
public class LearnerRecordService {

    private ILearnerRecordClient learnerRecordClient;

    public LearnerRecordService(ILearnerRecordClient learnerRecordClient) {
        this.learnerRecordClient = learnerRecordClient;
    }

    @PreAuthorize("hasAnyAuthority('DOWNLOAD_BOOKING_FEED')")
    public List<LearnerRecordEvent> listEvents() {
        return learnerRecordClient.getLearnerRecordEvents();
    }

    public List<Booking> getBookings(LocalDate from, LocalDate to) {
        return learnerRecordClient.getBookings(from, to);
    }

    public List<ModuleRecord> getModuleRecordsForLearners(LocalDate from, LocalDate to, List<String> learnerIds) {
        return learnerRecordClient.getModuleRecordsForDateRangeAndLearnerIds(from, to, learnerIds);
    }

    public List<ModuleRecord> getModulesRecordsForCourseIds(LocalDate from, LocalDate to, List<String> courseIds) {
        return learnerRecordClient.getModuleRecordsForCourseIds(from, to, courseIds);
    }
}
