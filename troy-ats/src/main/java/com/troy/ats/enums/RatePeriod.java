package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum RatePeriod {
    HOUR,
    DAY,
    WEEK,
    MONTH,
    YEAR;

    @JsonCreator
    public static RatePeriod fromValue(String value) {
        if (value == null) {
            return null;
        }

        return RatePeriod.valueOf(
                value.trim().toUpperCase(Locale.ROOT));
    }
}