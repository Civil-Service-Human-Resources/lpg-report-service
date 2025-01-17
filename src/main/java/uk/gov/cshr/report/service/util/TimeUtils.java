package uk.gov.cshr.report.service.util;

import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Service
public class TimeUtils {

    private final Clock clock;

    public TimeUtils(Clock clock) {
        this.clock = clock;
    }

    public String getNowToTimeZoneString(String timezone, String format) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(clock);
        ZoneOffset targetZoneOffset = ZoneOffset.of(timezone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return zonedDateTime.withZoneSameInstant(targetZoneOffset).format(formatter);
    }

    public LocalDateTime convertZonedDateTimeToTimezone(LocalDateTime dateTime, String timezone){
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.of("UTC"));
        ZoneId targetZoneId = ZoneId.of(timezone);
        ZonedDateTime targetZonedDateTime = zonedDateTime.withZoneSameInstant(targetZoneId);
        return targetZonedDateTime.toLocalDateTime();
    }

}
