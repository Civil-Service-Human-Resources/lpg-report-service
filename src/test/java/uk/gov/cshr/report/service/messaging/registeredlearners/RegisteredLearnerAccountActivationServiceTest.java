package uk.gov.cshr.report.service.messaging.registeredlearners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerDataType;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOperation;

import java.util.List;

import static org.mockito.Mockito.*;

class RegisteredLearnerAccountActivationServiceTest {

    RegisteredLearnerAccountActivateService messageService = mock(RegisteredLearnerAccountActivateService.class);
    private final List<RegisteredLearnerMessageService<?>> messageServices = List.of(
        messageService
    );

    private final RegisteredLearnerConverterService registeredLearnerConverterService = new RegisteredLearnerConverterService(
            new ObjectMapper(), messageServices
    );

    @Test
    public void testProcessMessage() throws JsonProcessingException {
        String message = """
                {
                    "operation": "UPDATE",
                    "dataType": "ACCOUNT_ACTIVATE",
                    "data": {
                                "uid": "uid-123",
                                "active": "true"
                             }
                }
                """;
        when(messageService.matches(RegisteredLearnerOperation.UPDATE, RegisteredLearnerDataType.ACCOUNT_ACTIVATE)).thenReturn(true);
        registeredLearnerConverterService.processMessage(message);
        verify(messageService, atMostOnce()).process(message);
    }
}
