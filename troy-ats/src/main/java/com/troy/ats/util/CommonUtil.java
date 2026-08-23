package com.troy.ats.util;

import com.troy.ats.constants.CommonConstants;
import com.troy.ats.dto.ActivityLogRequest;
import com.troy.ats.entity.ActivityLog;
import com.troy.ats.entity.Employee;
import com.troy.ats.enums.CvFormat;
import com.troy.ats.service.SessionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Introspector;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

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
        String countryCode = user.getCountry().getCode();
        ZoneId zoneId = getTimeZone(countryCode);
        return  zoneId;
    }

    public static LocalDateTime convertInstantToLocalDate(Instant dateTime, final SessionService sessionService){

        try{
            ZoneId zoneId = getZoneIdForCurrentUser(sessionService);
            ZonedDateTime DateTime = dateTime.atZone(zoneId);
            LocalDateTime date = DateTime.toLocalDateTime();
            return  date;

        } catch (RuntimeException e) {
            Instant instant = Objects.nonNull(dateTime) ? dateTime : Instant.now();
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }


    }

    public static MediaType getMediaType(CvFormat format) {
        return switch (format) {
            case PDF -> MediaType.APPLICATION_PDF;
            case DOC -> MediaType.parseMediaType("application/msword");
            case DOCX -> MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        };
    }

    public static String getEmailSubject(String emailType){

        return switch (emailType) {

            case CommonConstants.EMAIL_TYPE_CV_REQUEST -> CommonConstants.EMAIL_TYPE_CV_REQUEST_SUBJECT;
            case CommonConstants.EMAIL_TYPE_INTERVIEW_INVITATION -> CommonConstants.EMAIL_TYPE_INTERVIEW_INVITATION_SUBJECT;
            case CommonConstants.EMAIL_TYPE_FOLLOW_UP -> CommonConstants.EMAIL_TYPE_FOLLOW_UP_SUBJECT;
            case CommonConstants.EMAIL_TYPE_OFFER -> CommonConstants.EMAIL_TYPE_OFFER_SUBJECT;
            case CommonConstants.EMAIL_TYPE_JOINING_REMINDER -> CommonConstants.EMAIL_TYPE_JOINING_REMINDER_SUBJECT;
            default ->
                    throw new IllegalArgumentException("Unknown email type: " + emailType);
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

        List<String> extensions = List.of(
                ".pdf",".doc", ".docx", ".jpeg", ".png", ".webp", ".jpg"
        );

        if (!extensions.contains(extension)) {

            throw new IllegalArgumentException("Only PDF, DOC and DOCX files are supported");
        }
    }

    public static String generateCvId() {

        return "CV-" + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

    private static final Set<String> ALLOWED_IMAGE_TYPES =
            Set.of(
                    "image/jpeg",
                    "image/png",
                    "image/webp"
            );

    public static void validatePhoto(MultipartFile photo) {

        String contentType = photo.getContentType();
        if (!ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Only JPG, PNG and WebP images are allowed");
        }
    }

    public static String enumToStringFormat(String source){

        String newValue = source.substring(0, 1).toUpperCase() + source.substring(1).toLowerCase();
        return newValue;
    }

    public static String getCode(String name) {
        String cleaned = name.replaceAll("[^A-Za-z]", "").toUpperCase();

        if (cleaned.length() >= 2) {
            return cleaned.substring(0, 2);
        }

        return (cleaned + "X").substring(0, 2);
    }

    public static String getCodeWithOneLetter(String name) {
        String cleaned = name.replaceAll("[^A-Za-z]", "").toUpperCase();

        if (cleaned.length() >= 1) {
            return cleaned.substring(0, 1);
        }

        return (cleaned + "X").substring(0, 1);
    }

    public static void populateActivityLog(String entityType, UUID entityId, String filed, String oldValue,String newValue, List<ActivityLogRequest> activityLogs){

        ActivityLogRequest log = new ActivityLogRequest();
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setField(filed);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);

        activityLogs.add(log);
    }

    public static String getFieldName(Class<?> clazz, String setterName, Class<?> parameterType) {

        Method setter = null;
        try {
            setter = clazz.getMethod(setterName, parameterType);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return Introspector.decapitalize(setter.getName().substring(3));
    }

    public static   List<ActivityLog> logActivity(List<ActivityLogRequest> activityLogs, SessionService sessionService, boolean isUpdated) {

        List<ActivityLog> newActivityLogs = new ArrayList<>();
        for(ActivityLogRequest logRequest : CollectionUtils.emptyIfNull(activityLogs)){

            ActivityLog log = new ActivityLog();
            log.setEntityType(logRequest.getEntityType());
            log.setEntityId(logRequest.getEntityId());
            log.setOldValue(logRequest.getOldValue());
            log.setNewValue(logRequest.getNewValue());
            log.setPerformedBy(sessionService.getCurrentUser());
            log.setPerformedAt(Instant.now());
            if(StringUtils.isNotEmpty(logRequest.getAction())){
                log.setAction(logRequest.getAction());
                log.setDescription(logRequest.getDescription());
            } else if(isUpdated){
                log.setAction("Updated "+ logRequest.getField());
                log.setDescription("Updated "+ logRequest.getField() + " from " + logRequest.getOldValue() + " to "+logRequest.getNewValue());
            } else {
                log.setAction("Created "+ logRequest.getEntityType());
                log.setDescription("Created New "+ logRequest.getEntityType());
            }
            newActivityLogs.add(log);
        }

        //activityLogService.saveAll(logs);
        return newActivityLogs;
    }
}