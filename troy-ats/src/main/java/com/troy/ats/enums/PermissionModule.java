package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum PermissionModule {
    USER,
    ROLE,
    PERMISSION,
    CLIENT,
    JOB,
    CANDIDATE,
    SUBMISSION,
    INTERVIEW;

    @JsonCreator
    public static PermissionModule fromValue(String value) {
        if (value == null) {
            return null;
        }

        return PermissionModule.valueOf(
                value.trim().toUpperCase(Locale.ROOT));
    }
}