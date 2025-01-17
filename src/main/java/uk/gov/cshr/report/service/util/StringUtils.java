package uk.gov.cshr.report.service.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StringUtils {

    public String generateRandomString(Integer length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public String generateUid() {
        return UUID.randomUUID().toString();
    }
}
