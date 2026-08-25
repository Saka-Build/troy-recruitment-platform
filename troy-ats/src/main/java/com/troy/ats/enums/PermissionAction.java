package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum PermissionAction {
    READ,
    WRITE,
    DELETE;

    @JsonCreator
    public static PermissionAction fromValue(String value) {
        if (value == null) {
            return null;
        }

        return PermissionAction.valueOf(
                value.trim().toUpperCase(Locale.ROOT));
    }
}