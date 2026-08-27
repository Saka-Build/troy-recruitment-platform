package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum InterviewStatus {
    SCHEDULED, CONDUCTED, CANCELLED;

    @JsonCreator
    public static InterviewStatus fromValue(String value) {
        if (value == null) {
            return null;
        }

        return InterviewStatus.valueOf(
                value.trim().toUpperCase(Locale.ROOT));
    }
}