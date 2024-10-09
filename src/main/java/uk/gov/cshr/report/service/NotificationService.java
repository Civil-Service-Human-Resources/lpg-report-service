package uk.gov.cshr.report.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.client.notification.INotificationClient;
import uk.gov.cshr.report.dto.MessageDto;

@Service
public class NotificationService {
    private final INotificationClient notificationClient;

    @Value("${notification.successEmailTemplateName}")
    private String successEmailTemplateName;

    @Value("${notification.failureEmailTemplateName}")
    private String failureEmailTemplateName;

    public NotificationService(INotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void sendSuccessEmail(MessageDto messageDto){
        notificationClient.sendEmail(successEmailTemplateName, messageDto);
    }

    public void sendFailureEmail(MessageDto messageDto){
        notificationClient.sendEmail(failureEmailTemplateName, messageDto);
    }

}
