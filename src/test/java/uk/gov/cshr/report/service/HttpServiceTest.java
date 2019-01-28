package uk.gov.cshr.report.service;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import uk.gov.cshr.report.domain.learnerrecord.Booking;
import uk.gov.cshr.report.service.registry.domain.CivilServantResource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

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

    @Mock
    private ParameterizedTypeReferenceFactory parameterizedTypeReferenceFactory;

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
        ResponseEntity<List<Booking>> responseEntity = ResponseEntity.ok(Collections.singletonList(booking));


        ParameterizedTypeReference<List<Booking>> parameterizedTypeReference =
                new ParameterizedTypeReference<List<Booking>>() { };

        when(parameterizedTypeReferenceFactory.createListReference(Booking.class))
                .thenReturn(parameterizedTypeReference);

        when(restTemplate.exchange(requestEntity, parameterizedTypeReference)).thenReturn(responseEntity);

        assertEquals(Collections.singletonList(booking), httpService.getList(uri, Booking.class));
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
        ResponseEntity<Map<String, Booking>> responseEntity =
                ResponseEntity.ok(ImmutableMap.of("booking-id", booking));

        ParameterizedTypeReference<Map<String, Booking>> parameterizedTypeReference =
                new ParameterizedTypeReference<Map<String, Booking>>() { };

        when(parameterizedTypeReferenceFactory.createMapReference(Booking.class))
                .thenReturn(parameterizedTypeReference);

        when(restTemplate.exchange(requestEntity, parameterizedTypeReference)).thenReturn(responseEntity);

        assertEquals(ImmutableMap.of("booking-id", booking), httpService.getMap(uri, Booking.class));
    }

    @Test
    public void shouldReturnSingleResource() throws URISyntaxException {
        String accessToken = "access-token";

        when(accessTokenService.getAccessToken()).thenReturn(accessToken);

        HttpHeaders headers = mock(HttpHeaders.class);
        when(httpHeadersFactory.authorizationHeaders(accessToken)).thenReturn(headers);

        URI uri = new URI("http://example.org");

        RequestEntity requestEntity = mock(RequestEntity.class);
        when(requestEntityFactory.createGetRequest(headers, uri)).thenReturn(requestEntity);

        CivilServantResource civilServantResource = new CivilServantResource();

        ResponseEntity<CivilServantResource> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getBody()).thenReturn(civilServantResource);

        when(restTemplate.exchange(requestEntity, CivilServantResource.class)).thenReturn(responseEntity);

        assertEquals(civilServantResource, httpService.get(uri, CivilServantResource.class));
    }
}