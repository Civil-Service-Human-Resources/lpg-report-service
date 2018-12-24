package uk.gov.cshr.report.factory;

import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

@Component
public class UriBuilderFactory {
    public UriBuilder builder(String baseUri) {
        return new DefaultUriBuilderFactory(baseUri).builder();
    }
}
