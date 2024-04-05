package uk.gov.cshr.report.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.cshr.report.domain.aggregation.Aggregation;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregationResponse<A extends Aggregation> {

    private String delimiter;
    private List<A> results;
}
