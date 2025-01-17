package uk.gov.cshr.report.util.stub;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Getter
@RequiredArgsConstructor
public class StubService {

    private final IdentityServiceStubService identityServiceStubService;
    private final NotificationServiceStubService notificationServiceStubService;

    public void stubSendEmail(String emailName, String expBody) {
        identityServiceStubService.getClientToken();
        notificationServiceStubService.sendEmail(emailName, expBody);
    }

}
