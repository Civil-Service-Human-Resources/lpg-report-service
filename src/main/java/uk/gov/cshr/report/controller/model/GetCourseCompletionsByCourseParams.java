package uk.gov.cshr.report.controller.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetCourseCompletionsByCourseParams extends GetCourseCompletionsParams {

    @Size(min = 1, max = 30)
    @NotNull
    private List<String> courseIds;

}
