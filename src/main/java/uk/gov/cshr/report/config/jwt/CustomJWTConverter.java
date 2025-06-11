package uk.gov.cshr.report.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
@Slf4j
public class CustomJWTConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String[] AUTH_CLAIMS = {"authorities", "scope"};

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        log.debug("Extracting authorities from JWT");
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authClaim : AUTH_CLAIMS) {
            log.debug("Looking for claim {}", authClaim);
            Collection<String> authorities = jwt.getClaim(authClaim);
            if (authorities != null && !authorities.isEmpty()) {
                log.debug("claim {} found. Extracting authorities", authClaim);
                authorities.forEach(a -> grantedAuthorities.add(new SimpleGrantedAuthority(a.toUpperCase())));
                break;
            }
        }
        return grantedAuthorities;
    }

    @Override
    public <U> Converter<Jwt, U> andThen(Converter<? super Collection<GrantedAuthority>, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
