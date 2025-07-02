package uk.gov.cshr.report.service.messaging.registeredlearners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerDataType;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOperation;

import java.util.List;

import static org.mockito.Mockito.*;

class RegisteredLearnerConverterServiceTest {

    CreateRegisteredLearnerService messageService = mock(CreateRegisteredLearnerService.class);
    private final List<RegisteredLearnerMessageService<?>> messageServices = List.of(
        messageService
    );

    private RegisteredLearnerConverterService registeredLearnerConverterService = new RegisteredLearnerConverterService(
            new ObjectMapper(), messageServices
    );

    @Test
    public void testProcessMessage() throws JsonProcessingException {
        String message = """
                {
                    "metadata": {
                        "operation": "CREATE",
                        "dataType": "LEARNER_PROFILE",
                        "data": {}
                    }
                }
                """;
        when(messageService.matches(RegisteredLearnerOperation.CREATE, RegisteredLearnerDataType.LEARNER_PROFILE)).thenReturn(true);
        registeredLearnerConverterService.processMessage(message);
        verify(messageService, atMostOnce()).process(message);
    }

}
