package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum JobType {
    PERMANENT, CONTRACT, TEMPORARY, FREELANCE;

    @JsonCreator
    public static JobType fromValue(String value) {
        if (value == null) {
            return null;
        }

        return JobType.valueOf(
                value.trim().toUpperCase(Locale.ROOT));
    }
}