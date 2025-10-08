package uk.gov.cshr.report.service.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.domain.report.IDownloadableReportRequest;
import uk.gov.cshr.report.dto.MessageDto;
import uk.gov.cshr.report.service.util.IUtilService;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageDtoFactory {

    private final IUtilService utilService;

    @Value("${notification.successEmailTemplateName}")
    private String reportSuccessEmailTemplateName;

    @Value("${notification.failureEmailTemplateName}")
    private String reportFailureEmailTemplateName;

    public MessageDtoFactory(IUtilService utilService) {
        this.utilService = utilService;
    }

    private Map<String, String> getBasicReportEmailParams(IDownloadableReportRequest request) {
        String formattedDateTime = utilService.getNowToTimeZoneString(request.getRequesterTimezone(), "d MMMM yyyy HH:mm");
        Map<String, String> personalisation = new HashMap<>();
        personalisation.put("userName", request.getFullName());
        personalisation.put("exportDate", formattedDateTime);
        personalisation.put("reportType", request.getReportType().getName());
        return personalisation;
    }

    public MessageDto getReportExportSuccessEmail(IDownloadableReportRequest request) {
        Map<String, String> personalisation = getBasicReportEmailParams(request);
        personalisation.put("reportUrl", request.getFullDownloadUrl());
        String uid = utilService.generateUid();
        return new MessageDto(request.getRequesterEmail(), reportSuccessEmailTemplateName, personalisation,
                uid);
    }

    public MessageDto getReportExportFailureEmail(IDownloadableReportRequest request) {
        Map<String, String> personalisation = getBasicReportEmailParams(request);
        String uid = utilService.generateUid();
        return new MessageDto(request.getRequesterEmail(), reportFailureEmailTemplateName, personalisation,
                uid);
    }

}
