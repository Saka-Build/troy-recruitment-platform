package com.troy.ats.constants;

public final class CommonConstants {

    public static final String CANDIDATE_STATUS_ONBOARDED = "Onboarded";
    public static final String CANDIDATE_STATUS_READY_TO_SUBMIT = "Ready to Submit";
    public static final String CANDIDATE_SUBSTATUS_CANDIDATE_CONFIRMATION_AWAITED = "Candidate confirmation awaited";
    public static final String JOB_PRIORITY_URGENT = "urgent";
    public static final String STATUS_DROPDOWN = "statuses";
    public static final String SUB_STATUS_DROPDOWN = "substatuses";
    public static final String JOB_DROPDOWN = "jobs";
    public static final String CANDIDATE_CV_TYPE_TROY = "troy";

    public static final String EMAIL_TYPE_CV_REQUEST = "CV_REQUEST";
    public static final String EMAIL_TYPE_INTERVIEW_INVITATION = "INTERVIEW_INVITATION";
    public static final String EMAIL_TYPE_FOLLOW_UP = "FOLLOW_UP";
    public static final String EMAIL_TYPE_OFFER = "OFFER";
    public static final String EMAIL_TYPE_JOINING_REMINDER = "JOINING_REMINDER";

    public static final String EMAIL_TYPE_CV_REQUEST_SUBJECT = "Please find the candidate CV attached for your review.";
    public static final String EMAIL_TYPE_INTERVIEW_INVITATION_SUBJECT = "We would like to invite you for an interview.";
    public static final String EMAIL_TYPE_FOLLOW_UP_SUBJECT = "Follow upP";
    public static final String EMAIL_TYPE_OFFER_SUBJECT = "Offer Letter";
    public static final String EMAIL_TYPE_JOINING_REMINDER_SUBJECT = "Joining Letter";

    public static final String EMAIL_TEMPLATE_VARIABLE_CANDIDATE_NAME = "candidateName";
    public static final String EMAIL_TEMPLATE_VARIABLE_DESIGNATION = "designation";
    public static final String EMAIL_TEMPLATE_VARIABLE_EXPERIENCE = "experience";
    public static final String EMAIL_TEMPLATE_VARIABLE_LOCATION = "candidateName";
    public static final String EMAIL_TEMPLATE_PATH = "email/candidate-submission";


    private CommonConstants() {
        // Prevent instantiation
    }
}
