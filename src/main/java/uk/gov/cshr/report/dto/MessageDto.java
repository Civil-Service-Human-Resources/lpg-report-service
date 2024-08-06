package uk.gov.cshr.report.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    @Email(message="{message.recipient.valid}")
    @NotEmpty(message = "{message.recipient.required}")
    private String recipient;

    private Map<String, String> personalisation;

    String reference = UUID.randomUUID().toString();
}
