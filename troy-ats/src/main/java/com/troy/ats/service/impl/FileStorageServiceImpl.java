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

    private final Path uploadDirectory;

    public FileStorageServiceImpl(@Value("${file.upload-dir:uploads/cv}") String uploadDir) {

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
     * @param candidateId
     * @return
     */
    @Override
    public String store(MultipartFile file, UUID candidateId) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("CV file cannot be empty");
        }

        String extension = CommonUtil.getExtension(file.getOriginalFilename());
        CommonUtil.validateExtension(extension);

        String fileName = candidateId + extension;

        try {

            Path targetLocation = uploadDirectory.resolve(fileName).normalize();

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();

        } catch (IOException e) {

            throw new RuntimeException("Failed to store CV file", e);
        }



    }
}
