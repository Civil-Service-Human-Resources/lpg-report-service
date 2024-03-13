package uk.gov.cshr.report.factory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UriBuilderFactoryTest {
    private UriBuilderFactory uriBuilderFactory = new UriBuilderFactory();

    @Test
    public void shouldReturnUriBuilder() {
        String uri = "http://localhost/";

        assertNotNull(uriBuilderFactory.builder(uri));
    }
}