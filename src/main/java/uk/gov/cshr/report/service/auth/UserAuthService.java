package uk.gov.cshr.report.service.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
@Component
public class UserAuthService implements IUserAuthService {

    private final SecurityContextService securityContextService;

    public UserAuthService(SecurityContextService securityContextService) {
        this.securityContextService = securityContextService;
    }


    @Override
    public Jwt getBearerTokenFromUserAuth() {
        Object principal = getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwtPrincipal) {
            return jwtPrincipal;
        } else {
            return null;
        }
    }

    private Authentication getAuthentication() {
        return securityContextService.getSecurityContext().getAuthentication();
    }
}
