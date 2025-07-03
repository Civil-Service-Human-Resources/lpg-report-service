package uk.gov.cshr.report.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.cshr.report.configuration.TestConfig;
import uk.gov.cshr.report.domain.RegisteredLearner;
import uk.gov.cshr.report.integration.IntegrationTestBase;
import uk.gov.cshr.report.repository.RegisteredLearnerRepository;
import uk.gov.cshr.report.service.messaging.registeredlearners.RegisteredLearnerQueueClient;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

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

    @Test
    public void testAccountActivation() {
        createRegisteredLearner();

        registeredLearnerQueueClient.processMessage("""
                {
                    "messageId": "ID-1",
                    "messageTimestamp": "2025-01-01T11:00:00.0",
                    "metadata": {
                        "operation": "UPDATE",
                        "dataType": "ACCOUNT_ACTIVATE",
                        "data": {
                            "uid": "uid10000-0000-0000-0000-000000000000",
                            "active": "true"
                        }
                    }
                }
                """);

        Optional<RegisteredLearner> registeredLearnerOpt = registeredLearnerRepository.findById("uid10000-0000-0000-0000-000000000000");
        if(registeredLearnerOpt.isPresent()) {
            RegisteredLearner registeredLearner = registeredLearnerOpt.get();
            assertTrue(registeredLearner.isActive());
        }
    }

    private void createRegisteredLearner() {
        RegisteredLearner registeredLearner = new RegisteredLearner();
        registeredLearner.setUid("uid10000-0000-0000-0000-000000000000");
        registeredLearner.setActive(false);
        registeredLearner.setEmail("email@email.com");
        registeredLearner.setFullName("name");
        registeredLearner.setGradeId(1);
        registeredLearner.setGradeName("Grade 7");
        registeredLearner.setOrganisationId(1);
        registeredLearner.setOrganisationName("Cabinet Office");
        registeredLearner.setProfessionId(1);
        registeredLearner.setProfessionName("Analysis");

        Clock clock = Clock.fixed(Instant.parse("2025-01-01T10:00:00.000Z"), ZoneId.of("Europe/London"));
        ZonedDateTime zonedDateTime = clock.instant().atZone(clock.getZone());

        registeredLearner.setCreatedTimestamp(zonedDateTime);
        registeredLearner.setUpdatedTimestamp(zonedDateTime);

        registeredLearnerRepository.save(registeredLearner);
    }
}
