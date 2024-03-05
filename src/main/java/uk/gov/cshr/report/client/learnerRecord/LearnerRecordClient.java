package uk.gov.cshr.report.client.learnerRecord;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.domain.LearnerRecordEvent;
import uk.gov.cshr.report.domain.LearnerRecordSummary;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class LearnerRecordClient implements ILearnerRecordClient{

    private IHttpClient httpClient;

    @Value("${learnerRecord.bookingsUrl}")
    private String learnerRecordBookingsEndpointUrl;

    @Value("${learnerRecord.summariesUrl}")
    private String learnerRecordSummariesUrl;

    @Value("${learnerRecord.eventsUrl}")
    private String learnerRecordEventsUrl;

    @Value("${learnerRecord.moduleRecordsForLearnersUrl}")
    private String moduleRecordsForDateRangeAndLearnersUrl;

    @Value("${learnerRecord.moduleRecordsForCourseIdsUrl}")
    String moduleRecordsForCourseIdsUrl;

    public LearnerRecordClient(@Qualifier("learnerRecordHttpClient") IHttpClient httpClient){
        this.httpClient = httpClient;
    }
    @Override
    public List<Booking> getBookings(LocalDate from, LocalDate to) {
        String requestUrl = String.format("%s?from=%s&to=%s", learnerRecordBookingsEndpointUrl, from, to);
        RequestEntity<Void> request = RequestEntity.get(requestUrl).build();
        return httpClient.executeRequest(request, List.class);
    }

    @Override
    public List<LearnerRecordSummary> getLearnerRecordSummaries() {
        RequestEntity<Void> request = RequestEntity.get(learnerRecordSummariesUrl).build();
        List<LearnerRecordSummary> list = httpClient.executeRequest(request, List.class);
        return list;
    }

    @Override
    public List<LearnerRecordEvent> getLearnerRecordEvents() {
        RequestEntity<Void> request = RequestEntity.get(learnerRecordEventsUrl).build();
        return httpClient.executeRequest(request, List.class);
    }

    @Override
    public List<ModuleRecord> getModuleRecordsForDateRangeAndLearnerIds(LocalDate from, LocalDate to, String commaSeparatedLearnerIds) {
        String url = String.format("%s?from=%s&to=%s&learnerIds=%s", moduleRecordsForDateRangeAndLearnersUrl, from, to, commaSeparatedLearnerIds);
        RequestEntity<Void> request = RequestEntity.get(url).build();
        return httpClient.executeRequest(request, List.class);
    }

    @Override
    public List<ModuleRecord> getModuleRecordsForCourseIds(LocalDate from, LocalDate to, String commaSeparatedCourseIds) {
        String url = String.format("%s?from=%s&to=%s&courseIds=%s", moduleRecordsForCourseIdsUrl, from, to, commaSeparatedCourseIds);
        RequestEntity<Void> request = RequestEntity.get(url).build();
        return httpClient.executeRequest(request, List.class);
    }
}
