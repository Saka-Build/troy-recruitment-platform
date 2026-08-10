package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserRole {
    super_admin,
    admin,
    recruiter,
    resourcer,
    coordinator;

    @JsonCreator
    public static UserRole fromValue(String value) {
        if (value == null) {
            return null;
        }

        return UserRole.valueOf(
                value.trim().toLowerCase()
        );
    }


}