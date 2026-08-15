package com.troy.ats.service;

import com.troy.ats.entity.Candidate;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    /**
     *
     * @param candidate
     * emailType
     * @param file
     */
    void sendCandidateEmail(Candidate candidate, String emailType, MultipartFile file);
}

