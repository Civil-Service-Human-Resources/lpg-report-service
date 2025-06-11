package uk.gov.cshr.report.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CustomJWTConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String[] AUTH_CLAIMS = {"authorities", "scope"};

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        log.debug("Extracting authorities from JWT");
        Set<String> authoritiesSet = new HashSet<>();
        for (String authClaim : AUTH_CLAIMS) {
            log.debug("Looking for claim {}", authClaim);
            Collection<String> authorities = jwt.getClaim(authClaim);
            if (authorities != null && !authorities.isEmpty()) {
                log.debug("claim {} found. Extracting authorities", authClaim);
                authorities.forEach(a -> authoritiesSet.add(a.toUpperCase()));
            }
        }
        return authoritiesSet.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public <U> Converter<Jwt, U> andThen(Converter<? super Collection<GrantedAuthority>, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
