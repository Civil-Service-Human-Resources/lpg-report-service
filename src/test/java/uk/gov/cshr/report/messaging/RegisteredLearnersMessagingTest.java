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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
                            "uid":"uid10000-0000-0000-0000-000000000000",
                            "email":"email@email.com",
                            "fullName":"fullName",
                            "gradeId":1,
                            "gradeName":"Grade 7",
                            "organisationId":1,
                            "organisationName":"Cabinet Office",
                            "professionId":1,
                            "professionName":"Analysis"
                        }
                    }
                }
                """);
        assertTrue(registeredLearnerRepository.findById("uid10000-0000-0000-0000-000000000000").isPresent());
        Optional<RegisteredLearner> registeredLearnerOpt = registeredLearnerRepository.findById("uid10000-0000-0000-0000-000000000000");
        if(registeredLearnerOpt.isPresent()) {
            RegisteredLearner registeredLearner = registeredLearnerOpt.get();
            assertEquals("email@email.com", registeredLearner.getEmail());
            assertEquals("fullName", registeredLearner.getFullName());
            assertEquals(1, registeredLearner.getGradeId());
            assertEquals("Grade 7", registeredLearner.getGradeName());
            assertEquals(1, registeredLearner.getOrganisationId());
            assertEquals("Cabinet Office", registeredLearner.getOrganisationName());
            assertEquals(1, registeredLearner.getProfessionId());
            assertEquals("Analysis", registeredLearner.getProfessionName());
            assertEquals("2025-01-01T10:00", registeredLearner.getCreatedTimestamp().toString());
            assertEquals("2025-01-01T10:00", registeredLearner.getUpdatedTimestamp().toString());
        }
    }

    @Test
    public void testAccountActivation() {
        createRegisteredLearner();

        registeredLearnerQueueClient.processMessage("""
                {
                    "messageId": "ID-1",
                    "messageTimestamp": "2025-01-01T11:00:00",
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
            assertEquals("2025-01-01T11:00", registeredLearner.getUpdatedTimestamp().toString());
        }
    }

    @Test
    public void testOrganisationDelete() {
        createRegisteredLearner();

        registeredLearnerQueueClient.processMessage("""
                {
                    "messageId": "ID-1",
                    "messageTimestamp": "2025-01-01T11:00:00.0",
                    "metadata": {
                        "operation": "DELETE",
                        "dataType": "ORGANISATION",
                        "data": {
                            "organisationIds": [1,2,3]
                        }
                    }
                }
                """);

        Optional<RegisteredLearner> registeredLearnerOpt = registeredLearnerRepository.findById("uid10000-0000-0000-0000-000000000000");
        if(registeredLearnerOpt.isPresent()) {
            RegisteredLearner registeredLearner = registeredLearnerOpt.get();
            assertNull(registeredLearner.getOrganisationId());
            assertNull(registeredLearner.getOrganisationName());
            assertEquals("2025-01-01T11:00Z", registeredLearner.getUpdatedTimestamp().toString());
        }
    }

    @Test
    public void testEmailUpdate() {
        createRegisteredLearner();

        registeredLearnerQueueClient.processMessage("""
                {
                    "messageId": "ID-1",
                    "messageTimestamp": "2025-01-01T11:00",
                    "metadata": {
                        "operation": "UPDATE",
                        "dataType": "EMAIL_UPDATE",
                        "data": {
                            "uid": "uid10000-0000-0000-0000-000000000000",
                            "email": "updated_email@test.com"
                        }
                    }
                }
                """);

        Optional<RegisteredLearner> registeredLearnerOpt = registeredLearnerRepository.findById("uid10000-0000-0000-0000-000000000000");
        if(registeredLearnerOpt.isPresent()) {
            RegisteredLearner registeredLearner = registeredLearnerOpt.get();
            assertNull(registeredLearner.getOrganisationId());
            assertNull(registeredLearner.getOrganisationName());
            assertEquals("updated_email@test.com", registeredLearner.getEmail());
            assertEquals("2025-01-01T11:00", registeredLearner.getUpdatedTimestamp().toString());
        }
    }

    @Test
    public void testNameUpdate() {
        createRegisteredLearner();

        registeredLearnerQueueClient.processMessage("""
                {
                    "messageId":"ID",
                    "messageTimestamp":"2025-01-01T11:00",
                    "metadata": {
                        "operation":"UPDATE",
                        "dataType":"LEARNER_PROFILE",
                        "data":{
                            "uid": "uid10000-0000-0000-0000-000000000000",
                            "fullName":"updated_fullName"
                        }
                    }
                }
                """);
        assertTrue(registeredLearnerRepository.findById("uid10000-0000-0000-0000-000000000000").isPresent());
        Optional<RegisteredLearner> registeredLearnerOpt = registeredLearnerRepository.findById("uid10000-0000-0000-0000-000000000000");
        if(registeredLearnerOpt.isPresent()) {
            RegisteredLearner registeredLearner = registeredLearnerOpt.get();
            assertEquals("updated_fullName", registeredLearner.getFullName());
            assertEquals("2025-01-01T11:00", registeredLearner.getUpdatedTimestamp().toString());
        }
    }

    private void createRegisteredLearner() {
        RegisteredLearner registeredLearner = new RegisteredLearner();
        registeredLearner.setUid("uid10000-0000-0000-0000-000000000000");
        registeredLearner.setActive(false);
        registeredLearner.setEmail("email@email.com");
        registeredLearner.setFullName("fullName");
        registeredLearner.setGradeId(1);
        registeredLearner.setGradeName("Grade 7");
        registeredLearner.setOrganisationId(1);
        registeredLearner.setOrganisationName("Cabinet Office");
        registeredLearner.setProfessionId(1);
        registeredLearner.setProfessionName("Analysis");

        Clock clock = Clock.fixed(Instant.parse("2024-01-01T10:00:00.000Z"), ZoneId.of("UTC"));
        LocalDateTime dateTime = LocalDateTime.now(clock);

        registeredLearner.setCreatedTimestamp(dateTime);
        registeredLearner.setUpdatedTimestamp(dateTime);

        registeredLearnerRepository.save(registeredLearner);
    }
}
