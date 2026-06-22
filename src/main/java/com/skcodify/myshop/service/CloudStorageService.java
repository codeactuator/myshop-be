package com.skcodify.myshop.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class CloudStorageService {

    private static final Logger log = LoggerFactory.getLogger(CloudStorageService.class);

    private final Storage storage;

    @Value("${gcp.bucket.name}")
    private String bucketName;

    public CloudStorageService() {
        // Automatically resolves credentials via Google Application Default Credentials (ADC)
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    /**
     * Uploads a product image to GCS and returns its public URL.
     */
    public String uploadProductImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload an empty file.");
        }

        log.info("Starting file upload. Original name: {}, Size: {} bytes", file.getOriginalFilename(), file.getSize());

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFileName = "products/" + UUID.randomUUID().toString() + extension;

        BlobId blobId = BlobId.of(bucketName, uniqueFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        log.info("Uploading object to GCS path: gs://{}/{}", bucketName, uniqueFileName);
        storage.create(blobInfo, file.getBytes());

        String publicUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, uniqueFileName);
        log.info("Upload complete. Public URL: {}", publicUrl);
        return publicUrl;
    }

    /**
     * Uploads a shop banner image to GCS (in the banners/ folder) and returns its public URL.
     */
    public String uploadShopBanner(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload an empty file.");
        }

        log.info("Starting shop banner upload. Original name: {}, Size: {} bytes", file.getOriginalFilename(), file.getSize());

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFileName = "banners/" + UUID.randomUUID().toString() + extension;

        BlobId blobId = BlobId.of(bucketName, uniqueFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        log.info("Uploading shop banner to GCS path: gs://{}/{}", bucketName, uniqueFileName);
        storage.create(blobInfo, file.getBytes());

        String publicUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, uniqueFileName);
        log.info("Shop banner upload complete. Public URL: {}", publicUrl);
        return publicUrl;
    }
}