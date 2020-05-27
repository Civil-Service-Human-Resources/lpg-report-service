# Report Service

Generates reporting for LPG

### Local setup:
Service requires local H2 database enabled. In order to run service with local H2 database it needs to be enabled in the "application.yml" config file.
In order for service to generate proper report data according to local database changes, lpg report, CSRS, learner record and identity services need to connect local MySql database. Connection can be redirected to MySql by modifying the "application.yml" configuration file in each service.

### Tutorial how to install local MySql database:
https://dev.mysql.com/doc/mysql-osx-excerpt/5.7/en/osx-installation-pkg.html
