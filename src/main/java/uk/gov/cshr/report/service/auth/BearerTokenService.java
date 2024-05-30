package uk.gov.cshr.report.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.service.auth.exception.BlankBearerTokenException;

import static io.micrometer.common.util.StringUtils.isBlank;

@Service
@Slf4j
public class BearerTokenService implements IBearerTokenService {
    private final IUserAuthService userAuthService;

    public BearerTokenService(IUserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    public String getBearerToken() {
        String bearerToken = "";
        Jwt jwtPrincipal = userAuthService.getBearerTokenFromUserAuth();
        if (jwtPrincipal != null) {
            bearerToken = jwtPrincipal.getTokenValue();
        }
        if (isBlank(bearerToken)) {
            throw new BlankBearerTokenException("Bearer token is blank");
        }
        return bearerToken;
    }
}
