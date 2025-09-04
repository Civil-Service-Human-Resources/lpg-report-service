package uk.gov.cshr.report.config.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class RandomStringGenerator {

    public String generateRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

}
