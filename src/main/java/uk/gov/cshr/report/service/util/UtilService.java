package uk.gov.cshr.report.service.util;

import org.springframework.stereotype.Service;
import uk.gov.cshr.report.config.utils.RandomStringGenerator;
import uk.gov.cshr.report.config.utils.UUIDUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Service
public class UtilService implements IUtilService {

    private final Clock clock;
    private final RandomStringGenerator randomStringGenerator;
    private final UUIDUtils uuidUtils;

    public UtilService(Clock clock, RandomStringGenerator randomStringGenerator, UUIDUtils uuidUtils) {
        this.clock = clock;
        this.randomStringGenerator = randomStringGenerator;
        this.uuidUtils = uuidUtils;
    }

    @Override
    public String generateRandomString(Integer length) {
        return randomStringGenerator.generateRandomString(length);
    }

    @Override
    public String generateUid() {
        return uuidUtils.generateUUID();
    }

    @Override
    public LocalDateTime getNow(){
        return LocalDateTime.now(clock);
    }

    @Override
    public String getNowToTimeZoneString(String timezone, String format) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(clock);
        ZoneOffset targetZoneOffset = ZoneOffset.of(timezone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return zonedDateTime.withZoneSameInstant(targetZoneOffset).format(formatter);
    }

    @Override
    public LocalDateTime convertZonedDateTimeToTimezone(LocalDateTime dateTime, String timezone){
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.of("UTC"));
        ZoneId targetZoneId = ZoneId.of(timezone);
        ZonedDateTime targetZonedDateTime = zonedDateTime.withZoneSameInstant(targetZoneId);
        return targetZonedDateTime.toLocalDateTime();
    }
}
