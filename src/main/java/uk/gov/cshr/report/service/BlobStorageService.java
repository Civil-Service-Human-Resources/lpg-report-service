package uk.gov.cshr.report.service;

import com.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class BlobStorageService {
    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String azureBlobStorageConnectionString;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String azureBlobStorageContainerName;

    public BlobClient uploadFile(String fileName){
        BlobContainerClient blobContainerClient = getClient();

        if(!blobContainerClient.exists()){
            blobContainerClient.create();
        }

        BlobClient blobClient = blobContainerClient.getBlobClient(new File(fileName).getName());
        blobClient.uploadFromFile(fileName);
        return blobClient;
    }

    private BlobContainerClient getClient(){
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(azureBlobStorageConnectionString)
                .buildClient();
        return blobServiceClient.getBlobContainerClient(azureBlobStorageContainerName);
    }
}
