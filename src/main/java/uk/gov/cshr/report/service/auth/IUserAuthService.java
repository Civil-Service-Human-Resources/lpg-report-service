package uk.gov.cshr.report.service.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

public interface IUserAuthService {

    Jwt getBearerTokenFromUserAuth();
}
