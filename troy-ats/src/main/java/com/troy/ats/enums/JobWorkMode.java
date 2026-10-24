package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum JobWorkMode {
    ONSITE, REMOTE, HYBRID;

    @JsonCreator
    public static JobWorkMode fromValue(String value) {
        if (value == null) {
            return null;
        }

        return JobWorkMode.valueOf(
                value.trim().toUpperCase(Locale.ROOT));
    }
}