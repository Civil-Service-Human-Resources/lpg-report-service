package uk.gov.cshr.report.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.LearnerRecordSummary;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.emptyList;

@Service
public class LearnerRecordService {

    private OAuth2RestOperations restOperations;

    private String learnerRecordSummariesUrlFormat;

    @Autowired
    public LearnerRecordService(OAuth2RestOperations restOperations,
                                @Value("${learnerRecord.summariesUrlFormat}") String learnerRecordSummariesUrlFormat) {
        checkArgument(restOperations != null);
        this.restOperations = restOperations;
        this.learnerRecordSummariesUrlFormat = learnerRecordSummariesUrlFormat;
    }

    public List<LearnerRecordSummary> loadRecordsForOrganisation(String organisation) {
        return loadRecordsFor("organisation", organisation);
    }

    public List<LearnerRecordSummary> loadRecordsForProfession(String profession) {
        return loadRecordsFor("profession", profession);
    }

    private List<LearnerRecordSummary> loadRecordsFor(String type, String value) {
        LearnerRecordSummary[] summaries =
                restOperations.getForObject(
                        String.format(learnerRecordSummariesUrlFormat, type, value),
                        LearnerRecordSummary[].class);
        if (summaries == null) {
            return emptyList();
        }
        return Lists.newArrayList(summaries);
    }
}
