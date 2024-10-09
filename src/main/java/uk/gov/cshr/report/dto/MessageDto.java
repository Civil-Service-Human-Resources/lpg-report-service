package uk.gov.cshr.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private String recipient;

    private Map<String, String> personalisation;

    String reference = UUID.randomUUID().toString();
}
