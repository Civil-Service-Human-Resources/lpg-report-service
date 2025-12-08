package uk.gov.cshr.report.controller.model.reportRequest;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.cshr.report.validation.validUrl.CustomUrl;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestParams {
    @NotNull
    private String userId;

    @NotNull
    private String userEmail;

    @NotNull
    @CustomUrl
    private String downloadBaseUrl;

    @NotNull
    private String fullName;

    @NotNull
    private String timezone;
}
