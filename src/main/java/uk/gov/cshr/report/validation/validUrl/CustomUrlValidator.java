package uk.gov.cshr.report.validation.validUrl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.util.List;

@Component
public class CustomUrlValidator implements ConstraintValidator<CustomUrl, CharSequence> {

    private final List<String> protocols;
    private final String host;
    private final Integer port;

    public CustomUrlValidator(
            @Value("${app.validation.url.protocols}") List<String> protocols,
            @Nullable @Value("${app.validation.url.host:#{null}}") String host,
            @Nullable @Value("${app.validation.url.port:#{null}}") Integer port) {
        this.protocols = protocols;
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value != null && value.length() != 0) {
            java.net.URL url;
            try {
                url = new java.net.URL(value.toString());
            } catch (MalformedURLException e) {
                return false;
            }

            if (protocols.stream().noneMatch(p -> p.length() > 0 && url.getProtocol().equals(p))) {
                return false;
            } else if (this.host != null && this.host.length() > 0 && !url.getHost().equals(this.host)) {
                return false;
            } else {
                return this.port == null || url.getPort() == this.port;
            }
        } else {
            return true;
        }
    }
}
