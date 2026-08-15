package com.troy.ats.util;

import com.troy.ats.entity.Employee;
import com.troy.ats.enums.CountryEnum;
import com.troy.ats.enums.CvFormat;
import com.troy.ats.service.SessionService;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZoneId;
import java.util.UUID;

public final class CommonUtil {

    private CommonUtil() {
    }

    public static ZoneId getTimeZone(String countryCode) {

        return switch (countryCode.toUpperCase()) {
            case "IN" -> ZoneId.of("Asia/Kolkata");
            case "GB", "UK" -> ZoneId.of("Europe/London");
            default -> throw new IllegalArgumentException(
                    "Unsupported country code: " + countryCode
            );
        };
    }

    public static ZoneId getZoneIdForCurrentUser(final SessionService sessionService){

        Employee user = sessionService.getCurrentUser();
        String countryCode = CountryEnum.fromValue(user.getCountryCode()).getValue();
        ZoneId zoneId = getTimeZone(countryCode);
        return  zoneId;
    }

    public static MediaType getMediaType(CvFormat format) {
        return switch (format) {
            case PDF -> MediaType.APPLICATION_PDF;
            case DOC -> MediaType.parseMediaType("application/msword");
            case DOCX -> MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        };
    }

    public static CvFormat determineCvFormat(MultipartFile file) {

        String contentType = file.getContentType();

        if ("application/pdf".equalsIgnoreCase(contentType)) {
            return CvFormat.PDF;
        }

        if ("application/msword".equalsIgnoreCase(contentType)) {
            return CvFormat.DOC;
        }

        if ("application/vnd.openxmlformats-officedocument.wordprocessingml.document".equalsIgnoreCase(contentType)) {
            return CvFormat.DOCX;
        }

        throw new IllegalArgumentException(
                "Only PDF, DOC and DOCX files are supported"
        );
    }

    public static String getExtension(String fileName) {

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("Invalid file name");
        }

        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            throw new IllegalArgumentException("File must have an extension");
        }

        return fileName.substring(index).toLowerCase();
    }

    public static void validateExtension(String extension) {

        if (!extension.equals(".pdf") && !extension.equals(".doc") && !extension.equals(".docx")) {

            throw new IllegalArgumentException("Only PDF, DOC and DOCX files are supported");
        }
    }

    public static String generateCvId() {

        return "CV-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}