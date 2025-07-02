package uk.gov.cshr.report.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.cshr.report.configuration.TestConfig;
import uk.gov.cshr.report.integration.IntegrationTestBase;
import uk.gov.cshr.report.repository.RegisteredLearnerRepository;
import uk.gov.cshr.report.service.messaging.registeredlearners.RegisteredLearnerQueueClient;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(TestConfig.class)
@Transactional
public class RegisteredLearnersMessagingTest extends IntegrationTestBase {

    @Autowired
    private RegisteredLearnerRepository registeredLearnerRepository;

    @Autowired
    private RegisteredLearnerQueueClient registeredLearnerQueueClient;

    @Test
    public void testReadMessage() {
        registeredLearnerQueueClient.processMessage("""
                {
                    "messageId":"ID",
                    "messageTimestamp":"2025-01-01T10:00:00.0",
                    "metadata": {
                        "operation":"CREATE",
                        "dataType":"LEARNER_PROFILE",
                        "data":{
                            "uid":"CREATE_UID",
                            "email":"email@email.com",
                            "full_name":"name",
                            "organisation_id":1,
                            "organisation_name":"Cabinet Office",
                            "grade_id":1,
                            "grade_name":"Grade 7",
                            "profession_id":1,
                            "profession_name":"Analysis"
                        }
                    }
                }
                """);
        assertTrue(registeredLearnerRepository.findById("CREATE_UID").isPresent());
    }
}
