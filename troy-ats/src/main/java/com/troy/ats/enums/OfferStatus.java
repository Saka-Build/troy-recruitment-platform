package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum OfferStatus {
    PENDING, RELEASED, ACCEPTED, DECLINED, WITHDRAWN;

    @JsonCreator
    public static OfferStatus fromValue(String value) {
        if (value == null) {
            return null;
        }

        return OfferStatus.valueOf(
                value.trim().toUpperCase(Locale.ROOT));
    }
}