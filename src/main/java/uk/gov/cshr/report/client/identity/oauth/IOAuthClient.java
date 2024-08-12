package uk.gov.cshr.report.client.identity.oauth;

public interface IOAuthClient {
    String getAccessToken(String basicAuthClientId, String basicAuthClientSecret);
}
