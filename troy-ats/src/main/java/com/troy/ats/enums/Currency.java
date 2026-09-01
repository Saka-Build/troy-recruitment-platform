package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum Currency {
    USD,
    QAR,
    GBP,
    INR,
    PLN,
    EUR,
    AED,
    CAD;

    @JsonCreator
    public static Currency fromValue(String value) {
        if (value == null) {
            return null;
        }

        return Currency.valueOf(
                value.trim().toUpperCase(Locale.ROOT));
    }
}