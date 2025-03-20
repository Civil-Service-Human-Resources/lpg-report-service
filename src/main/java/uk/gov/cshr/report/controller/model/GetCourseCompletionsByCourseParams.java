package uk.gov.cshr.report.controller.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetCourseCompletionsByCourseParams extends GetCourseCompletionsParams {

    @Size(min = 1, max = 30)
    @NotNull
    private List<String> courseIds;

}
