package uk.gov.cshr.report.service.auth;

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
        Jwt jwt = null;
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            Object principal = getAuthentication().getPrincipal();
            if (principal instanceof Jwt jwtPrincipal) {
                jwt = jwtPrincipal;
            }
        }
        return jwt;
    }

    private Authentication getAuthentication() {
        return securityContextService.getSecurityContext().getAuthentication();
    }
}
