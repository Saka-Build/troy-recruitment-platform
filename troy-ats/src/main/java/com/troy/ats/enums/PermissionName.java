package com.troy.ats.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PermissionName {
    // User
    USER_CREATE,
    USER_READ,
    USER_UPDATE,
    USER_DELETE,

    // Role
    ROLE_CREATE,
    ROLE_READ,
    ROLE_UPDATE,
    ROLE_DELETE,

    // Permission
    PERMISSION_READ,

    // Job
    JOB_CREATE,
    JOB_READ,
    JOB_UPDATE,
    JOB_DELETE,

    // Candidate
    CANDIDATE_CREATE,
    CANDIDATE_READ,
    CANDIDATE_UPDATE,
    CANDIDATE_DELETE,

    // Submission
    SUBMISSION_CREATE,
    SUBMISSION_READ,
    SUBMISSION_UPDATE,
    SUBMISSION_DELETE,

    // Interview
    INTERVIEW_CREATE,
    INTERVIEW_READ,
    INTERVIEW_UPDATE,
    INTERVIEW_DELETE,
    INTERVIEW_FEEDBACK_UPDATE;

    @JsonCreator
    public static PermissionName fromValue(String value) {
        if (value == null) {
            return null;
        }

        return PermissionName.valueOf(
                value.trim().toLowerCase()
        );
    }


}

