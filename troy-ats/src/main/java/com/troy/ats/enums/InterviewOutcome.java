package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum InterviewOutcome {

    scheduled,
    completed,
    passed,
    failed,
    no_show,
    rescheduled,
    cancelled;

    @JsonCreator
    public static InterviewOutcome fromValue(String value) {
        if (value == null) {
            return null;
        }

        return InterviewOutcome.valueOf(
                value.trim().toLowerCase(Locale.ROOT));
    }
}
