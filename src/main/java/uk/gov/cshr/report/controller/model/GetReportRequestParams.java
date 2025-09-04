package uk.gov.cshr.report.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetReportRequestParams {
    @NotNull
    private String userId;

    @NotNull
    private List<String> status;
}
