package com.troy.ats.util;

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
}