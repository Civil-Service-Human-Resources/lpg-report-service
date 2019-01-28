package uk.gov.cshr.report.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import uk.gov.cshr.report.mapping.RoleMappingHandlerMapping;

import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RoleMappingHandlerMapping mapping = new RoleMappingHandlerMapping();
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return mapping;
    }

    private final ObjectMapper objectMapper;

    public WebMvcConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new CsvConverter<>(objectMapper));
    }
}
