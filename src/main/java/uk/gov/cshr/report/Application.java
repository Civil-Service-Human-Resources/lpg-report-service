package uk.gov.cshr.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Spring application configuration and entry point.
 */
@SpringBootApplication
@EnableAsync
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
