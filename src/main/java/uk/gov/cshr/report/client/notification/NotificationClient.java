package uk.gov.cshr.report.client.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import uk.gov.cshr.report.client.IHttpClient;
import uk.gov.cshr.report.dto.MessageDto;

@Slf4j
@Component
public class NotificationClient implements INotificationClient {
    private IHttpClient httpClient;

    @Value("${notification.sendEmailEndpointTemplate}")
    private String sendEmailEndpointTemplate;

    public NotificationClient(@Qualifier("notificationHttpClient") IHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void sendEmail(String accessToken, String templateName, MessageDto messageDto) {
        String emailEndpoint = sendEmailEndpointTemplate.replace("{{TEMPLATE_NAME}}", templateName);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        RequestEntity<MessageDto> request = RequestEntity
                .post(emailEndpoint)
                .headers(headers)
                .body(messageDto);

        httpClient.executeRequest(request, Void.class);
    }
}
