package uk.gov.cshr.report.config.utils;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDUtils {

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
