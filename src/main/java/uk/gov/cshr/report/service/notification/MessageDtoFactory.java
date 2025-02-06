package uk.gov.cshr.report.service.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.report.CourseCompletionReportRequest;
import uk.gov.cshr.report.dto.MessageDto;
import uk.gov.cshr.report.service.util.StringUtils;
import uk.gov.cshr.report.service.util.TimeUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageDtoFactory {

    private final TimeUtils timeUtils;
    private final StringUtils stringUtils;

    @Value("${notification.successEmailTemplateName}")
    private String courseCompletionReportSuccessEmailTemplateName;

    @Value("${notification.failureEmailTemplateName}")
    private String courseCompletionReportFailureEmailTemplateName;

    public MessageDtoFactory(TimeUtils timeUtils, StringUtils stringUtils) {
        this.timeUtils = timeUtils;
        this.stringUtils = stringUtils;
    }

    public MessageDto getCourseCompletionReportSuccessEmail(CourseCompletionReportRequest request) {
        String formattedDateTime = timeUtils.getNowToTimeZoneString(request.getRequesterTimezone(), "d MMMM yyyy HH:mm");
        Map<String, String> personalisation = new HashMap<>();
        personalisation.put("userName", request.getFullName());
        personalisation.put("exportDate", formattedDateTime);
        personalisation.put("reportUrl", request.getFullDownloadUrl());
        personalisation.put("reportType", "Course completions");
        String uid = stringUtils.generateUid();
        return new MessageDto(request.getRequesterEmail(), courseCompletionReportSuccessEmailTemplateName, personalisation,
                uid);
    }

    public MessageDto getCourseCompletionReportFailureEmail(CourseCompletionReportRequest request) {
        String formattedDateTime = timeUtils.getNowToTimeZoneString(request.getRequesterTimezone(), "d MMMM yyyy HH:mm");
        Map<String, String> personalisation = new HashMap<>();
        personalisation.put("userName", request.getFullName());
        personalisation.put("exportDate", formattedDateTime);
        personalisation.put("reportType", "Course completions");
        String uid = stringUtils.generateUid();
        return new MessageDto(request.getRequesterEmail(), courseCompletionReportFailureEmailTemplateName, personalisation,
                uid);
    }

}
