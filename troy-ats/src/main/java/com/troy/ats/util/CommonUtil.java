package com.troy.ats.util;

import com.troy.ats.entity.Employee;
import com.troy.ats.enums.CountryEnum;
import com.troy.ats.enums.CvFormat;
import com.troy.ats.service.SessionService;
import org.springframework.http.MediaType;

import java.time.ZoneId;

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
}