package uk.gov.cshr.report.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class MessageDto {

    private final String recipient;
    private final String templateName;
    private final Map<String, String> personalisation;
    private final String reference;

    @JsonIgnore
    public String getTemplateName() {
        return templateName;
    }
}
