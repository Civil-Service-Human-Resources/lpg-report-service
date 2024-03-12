package uk.gov.cshr.report.client.learnerRecord;

import uk.gov.cshr.report.domain.LearnerRecordEvent;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;

import java.time.LocalDate;
import java.util.List;

public interface ILearnerRecordClient {
    public List<Booking> getBookings(LocalDate from, LocalDate to);

    public List<LearnerRecordEvent> getLearnerRecordEvents();

    public List<ModuleRecord> getModuleRecordsForDateRangeAndLearnerIds(LocalDate from, LocalDate to, List<String> learnerIds);

    public List<ModuleRecord> getModuleRecordsForCourseIds(LocalDate from, LocalDate to, List<String> courseIds);
}
