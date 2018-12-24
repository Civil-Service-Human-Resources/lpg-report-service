package uk.gov.cshr.report.factory;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class UriBuilderFactoryTest {
    private UriBuilderFactory uriBuilderFactory = new UriBuilderFactory();

    @Test
    public void shouldReturnUriBuilder() {
        String uri = "http://localhost/";

        assertNotNull(uriBuilderFactory.builder(uri));
    }
}