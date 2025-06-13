package uk.gov.cshr.report.integration;

import org.testcontainers.containers.PostgreSQLContainer;

public class CustomPGContainer extends PostgreSQLContainer<CustomPGContainer> {
    private static final String IMAGE_VERSION = "postgres:16-alpine";
    private final static String pgDatabaseName = "reporting";
    private final static String pgUsername = "root";
    private final static String pgPassword = "root_pw";
    private static CustomPGContainer container;

    private CustomPGContainer() {
        super(IMAGE_VERSION);
    }

    public static CustomPGContainer getInstance() {
        if (container == null) {
            container = new CustomPGContainer()
                    .withDatabaseName(pgDatabaseName)
                    .withUsername(pgUsername)
                    .withPassword(pgPassword);
        }
        return container;
    }

}
