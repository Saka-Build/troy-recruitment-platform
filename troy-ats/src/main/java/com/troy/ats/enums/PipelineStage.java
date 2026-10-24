package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum PipelineStage {
    APPLIED, SCREENING, READY_TO_SUBMIT, SUBMITTED, INTERVIEW, SELECTED, REJECTED, ONBOARDING, ONBOARDED;

    @JsonCreator
    public static PipelineStage fromValue(String value) {
        if (value == null) {
            return null;
        }
        String normalizedValue = value.trim().toUpperCase(Locale.ROOT);

        try {
            return PipelineStage.valueOf(normalizedValue);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}