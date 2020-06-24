package uk.gov.cshr.report.validators;

import java.util.Collection;
import java.util.List;

import uk.gov.cshr.report.enums.ReporterRole;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.ImmutableList;

public class ReportRoleValidator {
    private static final List<SimpleGrantedAuthority> ACCEPTABLE_ROLES = ImmutableList.of(
        new SimpleGrantedAuthority(ReporterRole.ORGANIZATION_REPORTER.getValue()),
        new SimpleGrantedAuthority(ReporterRole.PROFESSION_REPORTER.getValue()),
        new SimpleGrantedAuthority(ReporterRole.CSHR_REPORTER.getValue())
    );

    private ReportRoleValidator() {
    }

    public static void validate(Collection<? extends GrantedAuthority> authorities) {
        if (!CollectionUtils.containsAny(authorities, ACCEPTABLE_ROLES)) {
            throw new IllegalStateException("No acceptable report generation roles present.");
        }

        if (CollectionUtils.intersection(authorities, ACCEPTABLE_ROLES).size() > 1) {
            throw new IllegalStateException("Too many report generation roles assigned to user.");
        }
    }
}
