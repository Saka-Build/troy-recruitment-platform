package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum JobStatus {

    OPEN, ON_HOLD, FILLED, CLOSED, CANCELLED;
    //open, on_hold, filled, closed, cancelled

    @JsonCreator
    public static JobStatus fromValue(String value) {
        if (value == null) {
            return null;
        }

        return JobStatus.valueOf(
                value.trim().toUpperCase(Locale.ROOT));
    }
}