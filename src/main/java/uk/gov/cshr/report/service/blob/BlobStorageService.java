package uk.gov.cshr.report.service.blob;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.exception.ReportNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.File;

@Service
@Slf4j
public class BlobStorageService {
    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String azureBlobStorageConnectionString;

    public ByteArrayOutputStream downloadFile(String blobStorageContainer, String filename) {
        log.info("Downloading file {} from blob container {}", filename, blobStorageContainer);
        BlobContainerClient blobContainerClient = getClient(blobStorageContainer);
        BlobClient blobClient = blobContainerClient.getBlobClient(filename);
        if (!blobClient.exists()){
            throw new ReportNotFoundException(String.format("Blob with filename '%s' was not found", filename));
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.downloadStream(outputStream);
        return outputStream;
    }

    public void uploadFile(String blobStorageContainer, String filename) {
        log.info("Uploading file {} to blob container {}", filename, blobStorageContainer);
        BlobContainerClient blobContainerClient = getClient(blobStorageContainer);
        if (!blobContainerClient.exists()){
            blobContainerClient.create();
        }
        BlobClient blobClient = blobContainerClient.getBlobClient(new File(filename).getName());
        blobClient.deleteIfExists();
        blobClient.uploadFromFile(filename);
    }

    private BlobContainerClient getClient(String blobStorageContainer){
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(azureBlobStorageConnectionString)
                .buildClient();
        return blobServiceClient.getBlobContainerClient(blobStorageContainer);
    }
}
