package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Service
public class AzureBlobService {

    private final BlobContainerClient containerClient;
    
    @Value("${azure.storage.container.name}")
    private String containerName;
    
    public AzureBlobService(
            @Value("${azure.storage.connection-string}") String connectionString,
            @Value("${azure.storage.container.name}") String containerName) {
        
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        
        this.containerClient = blobServiceClient.getBlobContainerClient(containerName);
        
        // Create the container if it doesn't exist
        if (!containerClient.exists()) {
            containerClient.create();
        }
    }
    
    public String uploadBlob(MultipartFile file) throws IOException {
        // Generate a unique name for the blob
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String blobName = UUID.randomUUID().toString() + extension;
        
        // Get a reference to a blob
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        
        // Upload the file
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        
        // Return the blob URL
        return blobClient.getBlobUrl();
    }
    
    public byte[] downloadBlob(String blobUrl) throws IOException {
        // Extract blob name from URL
        String blobName = blobUrl.substring(blobUrl.lastIndexOf("/") + 1);
        
        // Get a reference to a blob
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        
        // Download the blob
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        blobClient.download(outputStream);
        
        return outputStream.toByteArray();
    }
    
    public void deleteBlob(String blobUrl) {
        // Extract blob name from URL
        String blobName = blobUrl.substring(blobUrl.lastIndexOf("/") + 1);
        
        // Get a reference to a blob
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        
        // Delete the blob
        blobClient.deleteIfExists();
    }
} 