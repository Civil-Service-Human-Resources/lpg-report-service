package uk.gov.cshr.report.domain.registry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCivilServantsForUidsParams {

    private Collection<String> uids;
    private Integer organisationId = null;

}
