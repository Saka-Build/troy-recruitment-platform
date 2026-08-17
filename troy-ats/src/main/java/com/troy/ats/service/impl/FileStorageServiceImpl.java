package com.troy.ats.service.impl;


import com.troy.ats.service.FileStorageService;
import com.troy.ats.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service("fileStorageService")
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-original-cv-dir:uploads/originalcv}")
    private String uploadOriginalCVDir;

    @Value("${file.upload-troy-cv-dir:uploads/troycv}")
    private String uploadTroyCVDir;

    @Value("${file.upload-photo-dir:uploads/photo}")
    private String uploadPhotoDir;

    private Path uploadDirectory;


    public void setUploadDirectory(boolean isOriginalCV, boolean isPhoto) {

        String uploadDir = isPhoto ? uploadPhotoDir : isOriginalCV ? uploadOriginalCVDir : uploadTroyCVDir;
        this.uploadDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.uploadDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    /**
     *
     * @param file
     * @param id
     * @return
     */
    @Override
    public String store(MultipartFile file, UUID id, boolean isOriginalCV, boolean isPhoto) {

        setUploadDirectory(isOriginalCV, isPhoto);

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CV file cannot be empty");
        }

        String extension = CommonUtil.getExtension(file.getOriginalFilename());
        CommonUtil.validateExtension(extension);

        String fileName = id + extension;

        try {

            Path targetLocation = uploadDirectory.resolve(fileName).normalize();

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();

        } catch (IOException e) {

            throw new RuntimeException("Failed to store CV file", e);
        }



    }

    /**
     *
     * @param fileUrl
     */
    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {

            String fileName = Paths.get(fileUrl)
                    .getFileName()
                    .toString();

            Path file = uploadDirectory
                    .resolve(fileName)
                    .normalize();

            if (!file.startsWith(uploadDirectory)) {
                return;
            }

            Files.deleteIfExists(file);

        } catch (IOException e) {
            // Log instead of failing the candidate update
            System.err.println(
                    "Could not delete old CV: " + fileUrl
            );
        }
    }
}
