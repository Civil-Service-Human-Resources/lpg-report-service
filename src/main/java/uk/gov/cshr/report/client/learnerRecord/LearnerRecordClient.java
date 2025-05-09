package uk.gov.cshr.report.client.learnerRecord;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.domain.LearnerRecordEvent;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.domain.learnerrecord.FromToParamsCourseIds;
import uk.gov.cshr.report.domain.learnerrecord.FromToParamsUserIds;
import uk.gov.cshr.report.domain.learnerrecord.ModuleRecord;
import uk.gov.cshr.report.service.ParameterizedTypeReferenceFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static uk.gov.cshr.report.service.util.HttpUtils.batchList;

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

    @Value("${learningCatalogue.modulesForCourseIdsBatchSize}")
    private Integer moduleRecordBatchSize;

    @Value("${learnerRecord.moduleRecordsForLearnersUrl}")
    private String moduleRecordsForDateRangeAndLearnersUrl;

    @Value("${learnerRecord.moduleRecordsForCourseIdsUrl}")
    String moduleRecordsForCourseIdsUrl;

    ParameterizedTypeReferenceFactory parameterizedTypeReferenceFactory;

    public LearnerRecordClient(
            @Qualifier("learnerRecordHttpClient") IHttpClient httpClient,
            ParameterizedTypeReferenceFactory parameterizedTypeReferenceFactory){
        this.httpClient = httpClient;
        this.parameterizedTypeReferenceFactory = parameterizedTypeReferenceFactory;
    }
    @Override
    public List<Booking> getBookings(LocalDate from, LocalDate to) {
        String requestUrl = String.format("%s?from=%s&to=%s", learnerRecordBookingsEndpointUrl, from, to);
        RequestEntity<Void> request = RequestEntity.get(requestUrl).build();
        return httpClient.executeListRequest(request, parameterizedTypeReferenceFactory.createListReference(Booking.class));
    }

    @Override
    public List<LearnerRecordEvent> getLearnerRecordEvents() {
        RequestEntity<Void> request = RequestEntity.get(learnerRecordEventsUrl).build();
        return httpClient.executeListRequest(request, parameterizedTypeReferenceFactory.createListReference(LearnerRecordEvent.class));
    }

    @Override
    public List<ModuleRecord> getModuleRecordsForDateRangeAndLearnerIds(LocalDate from, LocalDate to, List<String> learnerIds) {
        List<ModuleRecord> moduleRecords = new ArrayList<>();
        batchList(moduleRecordBatchSize, learnerIds).forEach(batch -> {
            FromToParamsUserIds model = new FromToParamsUserIds(from, to, batch);
            RequestEntity<FromToParamsUserIds> request = RequestEntity.post(moduleRecordsForDateRangeAndLearnersUrl).body(model);
            List<ModuleRecord> response = httpClient.executeListRequest(request, parameterizedTypeReferenceFactory.createListReference(ModuleRecord.class));
            if (response != null) {
                moduleRecords.addAll(response);
            }
        });
        return moduleRecords;
    }

    @Override
    public List<ModuleRecord> getModuleRecordsForCourseIds(LocalDate from, LocalDate to, List<String> courseIds) {
        List<ModuleRecord> moduleRecords = new ArrayList<>();
        batchList(moduleRecordBatchSize, courseIds).forEach(batch -> {
            FromToParamsCourseIds model = new FromToParamsCourseIds(from, to, batch);
            RequestEntity<FromToParamsCourseIds> request = RequestEntity.post(moduleRecordsForCourseIdsUrl).body(model);
            List<ModuleRecord> response = httpClient.executeListRequest(request, parameterizedTypeReferenceFactory.createListReference(ModuleRecord.class));
            if (response != null) {
                moduleRecords.addAll(response);
            }
        });
        return moduleRecords;
    }
}
