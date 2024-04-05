package uk.gov.cshr.report.config.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ErrorHandler;

@Slf4j
public class CustomJmsErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable t) {
        log.error("Error reading JMS message : {}", t.getMessage());
        for (int i = 0; i < t.getStackTrace().length; i++) {
            StackTraceElement s = t.getStackTrace()[i];
            log.error(s.toString());
        }
    }

}
