package uk.gov.cshr.report.integration;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

public class CustomAzuriteContainer{
    public static GenericContainer<?> getInstance(){
        GenericContainer<?> azuriteContainer = new GenericContainer<>(DockerImageName.parse("mcr.microsoft.com/azure-storage/azurite"))
                .withExposedPorts(10000, 10001, 10002);

        List<String> portBindings = new ArrayList<>();
        portBindings.add("10000:10000");
        portBindings.add("10001:10001");
        portBindings.add("10002:10002");

        azuriteContainer.setPortBindings(portBindings);

        return azuriteContainer;
    }
}
