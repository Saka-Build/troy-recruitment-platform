package com.troy.ats.service;
import org.springframework.web.multipart.MultipartFile;
import java.net.URL;
import java.util.UUID;
public interface FileStorageService {

    /**
     * Uploads the file to S3 and returns the object key, which is what gets
     * persisted on the entity (originalCvUrl / troyCvUrl / photoUrl).
     *
     * @param file
     * @param id
     * @return
     */
    String store(MultipartFile file, UUID id, boolean isOriginalCV, boolean isPhoto);

    /**
     *
     * @param fileUrl
     * @param isOriginalCV
     * @param isPhoto
     */
    void delete(String fileUrl, boolean isOriginalCV, boolean isPhoto);

    /**
     * Time-limited URL the browser can download the object from directly.
     *
     * @param key
     * @param downloadFileName
     * @return
     */
    URL presignedUrl(String key, String downloadFileName);

    /**
     * How long a presigned URL stays valid.
     *
     * @return
     */
    long getPresignTtlMinutes();

}
