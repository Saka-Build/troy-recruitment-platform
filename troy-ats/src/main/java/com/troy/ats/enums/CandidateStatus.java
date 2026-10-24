package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum CandidateStatus {
    ACTIVE, INACTIVE, BLACKLISTED;

    @JsonCreator
    public static CandidateStatus fromValue(String value) {
        if (value == null) {
            return null;
        }

        return CandidateStatus.valueOf(
                value.trim().toUpperCase(Locale.ROOT));
    }
}