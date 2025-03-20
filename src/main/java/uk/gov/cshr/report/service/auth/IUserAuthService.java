package uk.gov.cshr.report.service.auth;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface IUserAuthService {

    Jwt getBearerTokenFromUserAuth();
    String getUsername();
    Boolean userHasRole(String role);
}
