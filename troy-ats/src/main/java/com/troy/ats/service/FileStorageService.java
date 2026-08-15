package com.troy.ats.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileStorageService {

    /**
     *
     * @param file
     * @param id
     * @return
     */
    String store(MultipartFile file, UUID id, boolean isOriginalCV, boolean isPhoto);

}

