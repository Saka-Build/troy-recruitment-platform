package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Locale;

public enum PipelineStage {
    APPLIED, SCREENING, READY_TO_SUBMIT, SUBMITTED, INTERVIEW, OFFER, JOINED;

    @JsonCreator
    public static PipelineStage fromValue(String value) {
        if (value == null) {
            return null;
        }

        return PipelineStage.valueOf(
                value.trim().toUpperCase(Locale.ROOT));
    }
}