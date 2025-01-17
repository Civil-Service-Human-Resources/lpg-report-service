package uk.gov.cshr.report.util;

import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.extension.RegisterExtension;
import uk.gov.cshr.report.configuration.NoKeepAliveTransformer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WireMockServer  {

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().port(9000)
                    .notifier(new ConsoleNotifier(true))
                    .extensions(NoKeepAliveTransformer.class))
            .failOnUnmatchedRequests(true)
            .configureStaticDsl(true).build();
}
