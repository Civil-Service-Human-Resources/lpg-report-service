package uk.gov.cshr.report.service.blob;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.temporal.ChronoUnit;

@Service
public class BlobStorageService {
    @Value("${spring.cloud.azure.storage.blob.connection-string}")
    private String azureBlobStorageConnectionString;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String azureBlobStorageContainerName;

    private final SasSignatureFactory sasSignatureFactory;

    public BlobStorageService(SasSignatureFactory sasSignatureFactory) {
        this.sasSignatureFactory = sasSignatureFactory;
    }

    public UploadResult uploadFileAndGenerateDownloadLink(String fileName, int daysToKeepLinkActive) {
        BlobClient blobClient = uploadFile(fileName);
        BlobServiceSasSignatureValues serviceSasSignatureValues = sasSignatureFactory.generateBlobServiceSasSignatureValues(daysToKeepLinkActive, ChronoUnit.DAYS);
        String downloadUrl = blobClient.generateSas(serviceSasSignatureValues);
        return new UploadResult(serviceSasSignatureValues.getStartTime(), serviceSasSignatureValues.getExpiryTime(),
                daysToKeepLinkActive, downloadUrl);
    }

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
