package uk.gov.cshr.report.service.messaging.registeredlearners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerDataType;
import uk.gov.cshr.report.service.messaging.registeredlearners.models.RegisteredLearnerOperation;

import java.util.List;

import static org.mockito.Mockito.*;

class RegisteredLearnerOrganisationDeleteServiceTest {

    RegisteredLearnerOrganisationDeleteService messageService = mock(RegisteredLearnerOrganisationDeleteService.class);
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
                    "messageId": "messageId",
                    "messageTimestamp": "2024-01-01T10:00:00.000Z",
                    "metadata": {
                        "operation": "DELETE",
                        "dataType": "ORGANISATION",
                        "data": {
                            "organisationIds": [1,2,3]
                        }
                    }
                }
                """;
        when(messageService.matches(RegisteredLearnerOperation.DELETE, RegisteredLearnerDataType.ORGANISATION)).thenReturn(true);
        registeredLearnerConverterService.processMessage(message);
        verify(messageService, times(1)).process(message);
    }
}
