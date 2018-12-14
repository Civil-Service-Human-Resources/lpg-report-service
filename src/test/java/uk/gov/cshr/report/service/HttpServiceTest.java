package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.client.RestTemplate;
import uk.gov.cshr.report.domain.learnerrecord.Booking;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HttpServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HttpHeadersFactory httpHeadersFactory;

    @Mock
    private RequestEntityFactory requestEntityFactory;

    @Mock
    private AccessTokenService accessTokenService;

    @InjectMocks
    private HttpService httpService;

    @Test
    public void shouldReturnListOfEntities() throws URISyntaxException {
        String accessToken = "access-token";

        when(accessTokenService.getAccessToken()).thenReturn(accessToken);

        HttpHeaders headers = mock(HttpHeaders.class);
        when(httpHeadersFactory.authorizationHeaders(accessToken)).thenReturn(headers);

        URI uri = new URI("http://example.org");

        RequestEntity requestEntity = mock(RequestEntity.class);
        when(requestEntityFactory.createGetRequest(headers, uri)).thenReturn(requestEntity);

        Booking booking = new Booking();
        ResponseEntity<Booking[]> responseEntity = ResponseEntity.ok(new Booking[] {booking});

        when(restTemplate.exchange(requestEntity, Booking[].class)).thenReturn(responseEntity);

        assertEquals(Collections.singletonList(booking), httpService.getList(uri, Booking[].class));
    }

    @Test
    public void shouldReturnMapOfEntities() throws URISyntaxException {
        String accessToken = "access-token";

        when(accessTokenService.getAccessToken()).thenReturn(accessToken);

        HttpHeaders headers = mock(HttpHeaders.class);
        when(httpHeadersFactory.authorizationHeaders(accessToken)).thenReturn(headers);

        URI uri = new URI("http://example.org");

        RequestEntity requestEntity = mock(RequestEntity.class);
        when(requestEntityFactory.createGetRequest(headers, uri)).thenReturn(requestEntity);

        Booking booking = new Booking();
        ResponseEntity<Map> responseEntity = ResponseEntity.ok(ImmutableMap.of("booking-id", booking));

        when(restTemplate.exchange(requestEntity, Map.class)).thenReturn(responseEntity);

        assertEquals(ImmutableMap.of("booking-id", booking), httpService.getMap(uri));
    }
}