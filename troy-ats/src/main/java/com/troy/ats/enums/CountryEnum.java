package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CountryEnum {
    IN("IN"),
    UK("UK");

    private final String value;

    CountryEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @JsonCreator
    public static CountryEnum fromValue(String value) {
        for (CountryEnum status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException(
                "Unknown JobStatus: " + value
        );
    }


}