package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum InterviewType {
    TEAMS, ZOOM, PHONE, ONSITE;

    @JsonCreator
    public static InterviewType fromValue(String value) {
        if (value == null) {
            return null;
        }

        return InterviewType.valueOf(
                value.trim().toUpperCase(Locale.ROOT));
    }
}