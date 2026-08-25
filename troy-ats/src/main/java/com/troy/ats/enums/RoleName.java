package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum RoleName {
    SUPER_ADMIN,
    ADMIN,
    LEAD_RECRUITER,
    RECRUITER;

    @JsonCreator
    public static RoleName fromValue(String value) {
        if (value == null) {
            return null;
        }

        return RoleName.valueOf(
                value.trim().toUpperCase(Locale.ROOT)
        );
    }


}