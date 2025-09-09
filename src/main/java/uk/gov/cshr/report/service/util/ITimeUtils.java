package uk.gov.cshr.report.service.util;

import java.time.LocalDateTime;

public interface ITimeUtils {
    LocalDateTime getNow();

    String getNowToTimeZoneString(String timezone, String format);

    LocalDateTime convertZonedDateTimeToTimezone(LocalDateTime dateTime, String timezone);
}
