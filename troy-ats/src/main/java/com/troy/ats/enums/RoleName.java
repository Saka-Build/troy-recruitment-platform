package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RoleName {
    super_admin,
    admin,
    recruiter,
    resourcer,
    coordinator;

    @JsonCreator
    public static RoleName fromValue(String value) {
        if (value == null) {
            return null;
        }

        return RoleName.valueOf(
                value.trim().toLowerCase()
        );
    }


}