package uk.gov.cshr.report.service.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TempDirectoryResource implements AutoCloseable {

    private final Path directory;

    public TempDirectoryResource(String dir) throws IOException {
        directory = Files.createTempDirectory(dir);
    }

    public Path getFile() {
        return directory;
    }

    @Override
    public void close() throws IOException {
        Files.deleteIfExists(directory);
    }
}
