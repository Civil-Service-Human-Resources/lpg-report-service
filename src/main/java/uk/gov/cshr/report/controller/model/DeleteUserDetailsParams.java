package uk.gov.cshr.report.controller.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class DeleteUserDetailsParams {
    @Size(min = 1, max = 50)
    @NotNull
    private List<String> uids;
}
