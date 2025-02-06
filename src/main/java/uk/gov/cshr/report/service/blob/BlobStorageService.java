package uk.gov.cshr.report.service.blob;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.cshr.report.exception.ReportNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.File;

@Service
public class BlobStorageService {
    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String azureBlobStorageConnectionString;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String azureBlobStorageContainerName;

    public ByteArrayOutputStream downloadFile(String filename) {
        BlobContainerClient blobContainerClient = getClient();
        BlobClient blobClient = blobContainerClient.getBlobClient(filename);
        if (!blobClient.exists()){
            throw new ReportNotFoundException(String.format("Blob with filename '%s' was not found", filename));
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.downloadStream(outputStream);
        return outputStream;
    }

    public void uploadFile(String fileName) {
        BlobContainerClient blobContainerClient = getClient();

        if (!blobContainerClient.exists()){
            blobContainerClient.create();
        }

        BlobClient blobClient = blobContainerClient.getBlobClient(new File(fileName).getName());
        blobClient.uploadFromFile(fileName);
    }

    private BlobContainerClient getClient(){
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(azureBlobStorageConnectionString)
                .buildClient();
        return blobServiceClient.getBlobContainerClient(azureBlobStorageContainerName);
    }
}
