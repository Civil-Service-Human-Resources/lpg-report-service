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

    public Authentication getAuthentication() {
        return securityContextService.getSecurityContext().getAuthentication();
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

    @Override
    public String getUsername() {
        String username = "";
        Jwt jwtPrincipal = getBearerTokenFromUserAuth();
        if (jwtPrincipal != null) {
            username = (String) jwtPrincipal.getClaims().get("user_name");
        }
        if (StringUtils.isBlank(username)) {
            throw new RuntimeException("Learner Id is missing from authentication token");
        }
        return username;
    }
}
