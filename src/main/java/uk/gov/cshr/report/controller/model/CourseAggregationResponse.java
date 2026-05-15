package uk.gov.cshr.report.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseAggregationResponse<A extends CourseAggregation> {
    private Collection<A> aggregations;
}
