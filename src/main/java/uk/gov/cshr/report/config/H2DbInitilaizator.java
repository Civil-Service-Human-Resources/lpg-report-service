package uk.gov.cshr.report.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class H2DbInitilaizator {
    private static final String H2_MIGRATIONS_PATH = "db/migration/h2/";

    @Autowired
    private void runFlywayMigrations(DataSource dataSource, Environment environment) {
        if (Boolean.valueOf(environment.getProperty("h2database.enabled", "false"))) {
            migrateSchema(dataSource, "learner_record", "learnerrecord");
            migrateSchema(dataSource, "identity", "identity");
            migrateSchema(dataSource, "csrs", "csrs");
        }
    }

    private void migrateSchema(DataSource dataSource, String schema, String folder) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setSchemas(schema);
        flyway.setLocations(H2_MIGRATIONS_PATH + folder);
        flyway.migrate();
    }
}
