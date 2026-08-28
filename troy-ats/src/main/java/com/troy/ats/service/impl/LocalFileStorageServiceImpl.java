package com.troy.ats.service.impl;

import com.troy.ats.exception.FileStorageException;
import com.troy.ats.service.FileStorageService;
import com.troy.ats.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
For LOcal Testinfn
 */
@Service("fileStorageService")
@Profile("!prod")
@Slf4j
public class LocalFileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-original-cv-dir:uploads/originalcv}")
    private String uploadOriginalCVDir;

    @Value("${file.upload-troy-cv-dir:uploads/troycv}")
    private String uploadTroyCVDir;

    @Value("${file.upload-photo-dir:uploads/photo}")
    private String uploadPhotoDir;


    private Path resolveUploadDirectory(boolean isOriginalCV, boolean isPhoto) {

        String uploadDir = isPhoto ? uploadPhotoDir : isOriginalCV ? uploadOriginalCVDir : uploadTroyCVDir;
        Path directory = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new FileStorageException("Could not create upload directory: " + directory, e);
        }

        return directory;
    }


    private String getFileType(boolean isOriginalCV, boolean isPhoto) {

        return isPhoto ? "PHOTO" : isOriginalCV ? "ORIGINAL_CV" : "TROY_CV";
    }


    @Override
    public String store(MultipartFile file, UUID id, boolean isOriginalCV, boolean isPhoto) {

        String fileType = getFileType(isOriginalCV, isPhoto);

        if (file == null || file.isEmpty()) {
            log.warn("Local store rejected - empty file. type={} entityId={}", fileType, id);
            throw new IllegalArgumentException("CV file cannot be empty");
        }

        String extension = CommonUtil.getExtension(file.getOriginalFilename());
        CommonUtil.validateExtension(extension);

        Path uploadDirectory = resolveUploadDirectory(isOriginalCV, isPhoto);
        Path targetLocation = uploadDirectory.resolve(id + extension).normalize();

        try {

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.info("Local store ok - type={} entityId={} path={} size={}B",
                    fileType, id, targetLocation, file.getSize());

            return targetLocation.toString();

        } catch (IOException e) {

            log.error("Local store failed - type={} entityId={} path={}", fileType, id, targetLocation, e);

            throw new FileStorageException("Failed to store file: " + targetLocation, e);
        }
    }

    /**
     * @param fileUrl absolute path returned by {@link #store}
     */
    @Override
    public void delete(String fileUrl, boolean isOriginalCV, boolean isPhoto) {

        String fileType = getFileType(isOriginalCV, isPhoto);

        if (fileUrl == null || fileUrl.isBlank()) {
            log.debug("Local delete skipped - no path held for type={}", fileType);
            return;
        }

        try {

            Path uploadDirectory = resolveUploadDirectory(isOriginalCV, isPhoto);
            Path file = uploadDirectory.resolve(Paths.get(fileUrl).getFileName()).normalize();

            // Only ever delete inside the configured directory, whatever the stored value says.
            if (!file.startsWith(uploadDirectory)) {
                log.warn("Local delete refused - path escapes upload dir. type={} path={}", fileType, fileUrl);
                return;
            }

            boolean deleted = Files.deleteIfExists(file);

            log.info("Local delete {} - type={} path={}", deleted ? "ok" : "no-op (absent)", fileType, file);

        } catch (IOException e) {

            // Log instead of failing the candidate update
            log.warn("Local delete failed - type={} path={}", fileType, fileUrl, e);
        }
    }


    @Override
    public URL presignedUrl(String key, String downloadFileName) {

        try {

            URL url = Paths.get(key).toAbsolutePath().normalize().toUri().toURL();

            log.info("Local URL issued - path={} fileName={}", key, downloadFileName);

            return url;

        } catch (MalformedURLException | RuntimeException e) {

            log.error("Local URL failed - path={}", key, e);

            throw new FileStorageException("Failed to generate download URL for: " + key, e);
        }
    }

    /** Not required */
    @Override
    public long getPresignTtlMinutes() {
        return 0L;
    }
}
