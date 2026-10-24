package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum InterviewRound {
    TECHNICAL, HR, FINAL, SCREENING, OTHER;

    @JsonCreator
    public static InterviewRound fromValue(String value) {
        if (value == null) {
            return null;
        }

        return InterviewRound.valueOf(
                value.trim().toUpperCase(Locale.ROOT));
    }
}