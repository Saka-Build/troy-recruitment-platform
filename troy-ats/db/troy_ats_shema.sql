-- ============================================================
-- TROY ATS — PostgreSQL Schema
-- Version 1.0
-- ============================================================

-- ============================================================
-- EXTENSIONS
-- ============================================================
CREATE EXTENSION IF NOT EXISTS "pgcrypto";  -- for gen_random_uuid()


-- ============================================================
-- ENUMS
-- ============================================================

CREATE TYPE user_role AS ENUM (
    'super_admin', 'admin', 'recruiter', 'resourcer', 'coordinator'
);

CREATE TYPE job_status AS ENUM (
    'open', 'on_hold', 'filled', 'closed', 'cancelled'
);

CREATE TYPE job_work_mode AS ENUM (
    'onsite', 'remote', 'hybrid'
);

CREATE TYPE job_type AS ENUM (
    'permanent', 'contract', 'temporary', 'freelance'
);

CREATE TYPE interview_type AS ENUM (
    'teams', 'zoom', 'phone', 'onsite'
);

CREATE TYPE interview_round AS ENUM (
    'technical', 'hr', 'final', 'screening', 'other'
);

CREATE TYPE ai_recommendation AS ENUM (
    'highly_recommended', 'recommended', 'consider_with_review', 'not_recommended'
);

CREATE TYPE message_channel AS ENUM (
    'outlook', 'gmail', 'whatsapp'
);

CREATE TYPE cv_format AS ENUM (
    'pdf', 'doc', 'docx'
);

CREATE TYPE pipeline_stage AS ENUM (
    'applied', 'screening', 'submitted', 'interview', 'offer', 'joined'
);

CREATE TYPE offer_status AS ENUM (
    'pending', 'released', 'accepted', 'declined', 'withdrawn'
);


-- ============================================================
-- 1. CLIENTS
-- ============================================================
CREATE TABLE clients (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(255)    NOT NULL,
    contact_person  VARCHAR(255),
    email           VARCHAR(255)    CHECK (
                                        email IS NULL OR
                                        email ~* '^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$'
                                    ),
    phone           VARCHAR(30),
    whatsapp        VARCHAR(30),
    country         VARCHAR(100),
    industry        VARCHAR(100),
    status          VARCHAR(50)     NOT NULL DEFAULT 'active'
                                    CHECK (status IN ('active', 'inactive', 'prospect')),
    address         TEXT,
    notes           TEXT,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by      UUID            -- FK to employees added after employees table
);

CREATE INDEX idx_clients_name    ON clients (name);
CREATE INDEX idx_clients_status  ON clients (status);
CREATE INDEX idx_clients_country ON clients (country);


-- ============================================================
-- 2. EMPLOYEES  (Troy team + system users)
-- ============================================================
CREATE TABLE employees (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_code   VARCHAR(50)     NOT NULL UNIQUE,
    full_name       VARCHAR(255)    NOT NULL,
    designation     VARCHAR(150)    NOT NULL,
    official_email  VARCHAR(255)    NOT NULL UNIQUE
                                    CHECK (official_email ~* '^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$'),
    personal_email  VARCHAR(255)    UNIQUE
                                    CHECK (
                                        personal_email IS NULL OR
                                        personal_email ~* '^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$'
                                    ),
    phone           VARCHAR(30)     NOT NULL,
    whatsapp        VARCHAR(30)     NOT NULL,
    photo_url       TEXT,
    role            user_role       NOT NULL DEFAULT 'recruiter',
    password_hash   TEXT,           -- store bcrypt / argon2 hash ONLY, never plain-text
    is_active       BOOLEAN         NOT NULL DEFAULT TRUE,
    last_login_at   TIMESTAMPTZ,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    failed_login_attempts  INT      NOT NULL DEFAULT 0,
    locked_until    TIMESTAMP       WITH TIME ZONE
);

CREATE INDEX idx_employees_official_email ON employees (official_email);
CREATE INDEX idx_employees_role           ON employees (role);
CREATE INDEX idx_employees_is_active      ON employees (is_active);

-- Back-fill FK on clients now that employees exists
ALTER TABLE clients
    ADD CONSTRAINT fk_clients_created_by
    FOREIGN KEY (created_by) REFERENCES employees (id) ON DELETE SET NULL;


-- ============================================================
-- 3. STATUS MASTER  (administrator-configurable)
-- ============================================================
CREATE TABLE statuses (
    id          UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100)    NOT NULL UNIQUE,
    colour_hex  VARCHAR(7)         NOT NULL DEFAULT '#6B7280'
                                CHECK (colour_hex ~* '^#[0-9A-Fa-f]{6}$'),
    sort_order  SMALLINT        NOT NULL DEFAULT 0,
    is_active   BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX idx_statuses_name_active ON statuses (name) WHERE is_active = TRUE;

-- Seed common statuses
INSERT INTO statuses (name, colour_hex, sort_order) VALUES
    ('Pipeline',          '#6B7280', 1),
    ('Actively Sourcing', '#3B82F6', 2),
    ('Ready to Submit',   '#8B5CF6', 3),
    ('Submitted',         '#F59E0B', 4),
    ('Interview',         '#EF4444', 5),
    ('Selected',          '#10B981', 6),
    ('Offer Released',    '#059669', 7),
    ('Onboarding',        '#0EA5E9', 8),
    ('Onboarded',         '#16A34A', 9),
    ('Hold',              '#D97706', 10),
    ('Rejected',          '#DC2626', 11),
    ('Closed',            '#374151', 12);


-- ============================================================
-- 4. SUB-STATUS MASTER  (mapped to a parent status)
-- ============================================================
CREATE TABLE sub_statuses (
    id          UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    status_id   UUID            NOT NULL REFERENCES statuses (id) ON DELETE CASCADE,
    name        VARCHAR(100)    NOT NULL,
    colour_hex  VARCHAR(7)         CHECK (
                                    colour_hex IS NULL OR
                                    colour_hex ~* '^#[0-9A-Fa-f]{6}$'
                                ),
    sort_order  SMALLINT        NOT NULL DEFAULT 0,
    is_active   BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    UNIQUE (status_id, name)
);

CREATE INDEX idx_sub_statuses_status_id ON sub_statuses (status_id);


-- ============================================================
-- 5. CANDIDATES
-- ============================================================
CREATE TABLE candidates (
    id                  UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    cv_id               VARCHAR(50)     NOT NULL UNIQUE,
    full_name           VARCHAR(255)    NOT NULL,
    email               VARCHAR(255)    CHECK (
                                            email IS NULL OR
                                            email ~* '^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$'
                                        ),
    phone               VARCHAR(30),
    whatsapp            VARCHAR(30),
    location            VARCHAR(255),
    nationality         VARCHAR(100),
    current_designation VARCHAR(150),
    current_employer    VARCHAR(255),
    experience_years    NUMERIC(4,1)    CHECK (experience_years IS NULL OR experience_years >= 0),
    notice_period_days  SMALLINT        CHECK (notice_period_days IS NULL OR notice_period_days >= 0),
    current_salary      NUMERIC(14,2)   CHECK (current_salary IS NULL OR current_salary >= 0),
    expected_salary     NUMERIC(14,2)   CHECK (expected_salary IS NULL OR expected_salary >= 0),
    salary_currency     VARCHAR(10)         DEFAULT 'USD',
    skills              TEXT[],
    education           TEXT,
    visa_status         VARCHAR(100),
    linkedin_url        TEXT,
    source              VARCHAR(100),

    -- Status
    status_id           UUID            REFERENCES statuses (id) ON DELETE RESTRICT,
    sub_status_id       UUID            REFERENCES sub_statuses (id) ON DELETE RESTRICT,

    -- Ownership
    cv_owner_id         UUID            NOT NULL REFERENCES employees (id) ON DELETE RESTRICT,
    referred_by         VARCHAR(255),
    reference_note      TEXT,

    -- CV file references (storage paths / S3 keys)
    original_cv_url     TEXT,
    original_cv_format  cv_format,
    troy_cv_url         TEXT,
    troy_cv_pdf_url     TEXT,

    is_active           BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by          UUID            REFERENCES employees (id) ON DELETE SET NULL,
    updated_by          UUID            REFERENCES employees (id) ON DELETE SET NULL,

    -- Guard: sub_status requires a status to be set
    CONSTRAINT chk_sub_status_requires_status CHECK (
        sub_status_id IS NULL OR status_id IS NOT NULL
    )
);

CREATE INDEX idx_candidates_skills_gin   ON candidates USING GIN (skills);
CREATE INDEX idx_candidates_full_name    ON candidates (full_name);
CREATE INDEX idx_candidates_status_id   ON candidates (status_id);
CREATE INDEX idx_candidates_cv_owner_id ON candidates (cv_owner_id);
CREATE INDEX idx_candidates_location    ON candidates (location);
CREATE INDEX idx_candidates_created_at  ON candidates (created_at DESC);
CREATE INDEX idx_candidates_is_active   ON candidates (is_active);


-- ============================================================
-- 6. JOBS / ROLES
-- ============================================================
CREATE TABLE jobs (
    id                  UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    title               VARCHAR(255)    NOT NULL,
    client_id           UUID            NOT NULL REFERENCES clients (id) ON DELETE RESTRICT,
    location            VARCHAR(255),
    country             VARCHAR(100),
    work_mode           job_work_mode,
    job_type            job_type,
    industry            VARCHAR(100),
    experience_min      NUMERIC(4,1)    CHECK (experience_min IS NULL OR experience_min >= 0),
    experience_max      NUMERIC(4,1)    CHECK (experience_max IS NULL OR experience_max >= 0),
    salary_min          NUMERIC(14,2)   CHECK (salary_min IS NULL OR salary_min >= 0),
    salary_max          NUMERIC(14,2)   CHECK (salary_max IS NULL OR salary_max >= 0),
    salary_currency     VARCHAR(10)     DEFAULT 'USD',
    skills_required     TEXT[],
    status              job_status      NOT NULL DEFAULT 'open',
    priority            VARCHAR(20)     NOT NULL DEFAULT 'medium'
                                        CHECK (priority IN ('low', 'medium', 'high', 'urgent')),
    description         TEXT,
    description_source  VARCHAR(30)     DEFAULT 'manual'
                                        CHECK (description_source IN
                                            ('manual', 'template', 'parsed', 'ai_generated', 'copilot')),
    is_template         BOOLEAN         NOT NULL DEFAULT FALSE,
    template_name       VARCHAR(255),
    ats_keywords        TEXT[],
    openings_count      SMALLINT        NOT NULL DEFAULT 1 CHECK (openings_count >= 1),
    filled_count        SMALLINT        NOT NULL DEFAULT 0 CHECK (filled_count >= 0),
    owner_id            UUID            REFERENCES employees (id) ON DELETE SET NULL,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    created_by          UUID            REFERENCES employees (id) ON DELETE SET NULL,

    CONSTRAINT chk_experience_range CHECK (
        experience_max IS NULL OR experience_min IS NULL OR experience_max >= experience_min
    ),
    CONSTRAINT chk_salary_range CHECK (
        salary_max IS NULL OR salary_min IS NULL OR salary_max >= salary_min
    ),
    CONSTRAINT chk_filled_le_openings CHECK (
        filled_count <= openings_count
    )
);

CREATE INDEX idx_jobs_client_id  ON jobs (client_id);
CREATE INDEX idx_jobs_status     ON jobs (status);
CREATE INDEX idx_jobs_priority   ON jobs (priority);
CREATE INDEX idx_jobs_skills_gin ON jobs USING GIN (skills_required);
CREATE INDEX idx_jobs_created_at ON jobs (created_at DESC);
CREATE INDEX idx_jobs_is_template ON jobs (is_template);


-- ============================================================
-- 7. SUBMISSIONS  (Candidate ↔ Job link)
-- ============================================================
CREATE TABLE submissions (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    candidate_id    UUID            NOT NULL REFERENCES candidates (id) ON DELETE CASCADE,
    job_id          UUID            NOT NULL REFERENCES jobs (id) ON DELETE CASCADE,
    pipeline_stage  pipeline_stage  NOT NULL DEFAULT 'applied',
    status_id       UUID            REFERENCES statuses (id) ON DELETE RESTRICT,
    sub_status_id   UUID            REFERENCES sub_statuses (id) ON DELETE RESTRICT,
    submitted_by    UUID            REFERENCES employees (id) ON DELETE SET NULL,
    submitted_at    TIMESTAMPTZ,
    notes           TEXT,
    is_active       BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

    UNIQUE (candidate_id, job_id)
);

CREATE INDEX idx_submissions_candidate_id ON submissions (candidate_id);
CREATE INDEX idx_submissions_job_id       ON submissions (job_id);
CREATE INDEX idx_submissions_stage        ON submissions (pipeline_stage);


-- ============================================================
-- 8. INTERVIEWS
-- ============================================================
CREATE TABLE interviews (
    id                  UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id       UUID            NOT NULL REFERENCES submissions (id) ON DELETE CASCADE,
    candidate_id        UUID            NOT NULL REFERENCES candidates (id) ON DELETE CASCADE,
    job_id              UUID            NOT NULL REFERENCES jobs (id) ON DELETE CASCADE,
    interview_date      DATE            NOT NULL,
    interview_time      TIME,
    interview_type      interview_type,
    round               interview_round,
    interviewer_name    VARCHAR(255),
    interviewer_email   VARCHAR(255)    CHECK (
                                            interviewer_email IS NULL OR
                                            interviewer_email ~* '^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$'
                                        ),
    meeting_link        TEXT,
    outcome             VARCHAR(20)     CHECK (outcome IN (
                                            'scheduled', 'completed', 'passed', 'failed',
                                            'no_show', 'rescheduled', 'cancelled'
                                        )),
    feedback            TEXT,
    scheduled_by        UUID            REFERENCES employees (id) ON DELETE SET NULL,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_interviews_candidate_id   ON interviews (candidate_id);
CREATE INDEX idx_interviews_job_id         ON interviews (job_id);
CREATE INDEX idx_interviews_interview_date ON interviews (interview_date);
-- Partial index for "today's interviews" dashboard query
CREATE INDEX idx_interviews_upcoming       ON interviews (interview_date)
    WHERE outcome IN ('scheduled', 'rescheduled');


-- ============================================================
-- 9. OFFERS
-- ============================================================
CREATE TABLE offers (
    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id   UUID            NOT NULL UNIQUE REFERENCES submissions (id) ON DELETE CASCADE,
    candidate_id    UUID            NOT NULL REFERENCES candidates (id) ON DELETE CASCADE,
    job_id          UUID            NOT NULL REFERENCES jobs (id) ON DELETE CASCADE,
    offered_salary  NUMERIC(14,2)   CHECK (offered_salary IS NULL OR offered_salary >= 0),
    salary_currency  VARCHAR(10)         DEFAULT 'USD',
    joining_date    DATE,
    offer_status    offer_status    NOT NULL DEFAULT 'pending',
    offer_letter_url TEXT,
    released_at     TIMESTAMPTZ,
    accepted_at     TIMESTAMPTZ,
    declined_at     TIMESTAMPTZ,
    decline_reason  TEXT,
    notes           TEXT,
    created_by      UUID            REFERENCES employees (id) ON DELETE SET NULL,
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_offers_candidate_id ON offers (candidate_id);
CREATE INDEX idx_offers_job_id       ON offers (job_id);
CREATE INDEX idx_offers_status       ON offers (offer_status);


-- ============================================================
-- 10. ONBOARDING
-- ============================================================
CREATE TABLE onboarding (
    id                      UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    offer_id                UUID        NOT NULL UNIQUE REFERENCES offers (id) ON DELETE CASCADE,
    candidate_id            UUID        NOT NULL REFERENCES candidates (id) ON DELETE CASCADE,
    job_id                  UUID        NOT NULL REFERENCES jobs (id) ON DELETE CASCADE,
    joining_confirmed_at    TIMESTAMPTZ,
    security_clearance_at   TIMESTAMPTZ,
    onboarding_started_at   TIMESTAMPTZ,
    onboarded_at            TIMESTAMPTZ,   -- set → counted as a placement
    notes                   TEXT,
    managed_by              UUID        REFERENCES employees (id) ON DELETE SET NULL,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_onboarding_candidate_id ON onboarding (candidate_id);
CREATE INDEX idx_onboarding_onboarded_at ON onboarding (onboarded_at);


-- ============================================================
-- 11. AI REVIEWS
-- ============================================================
CREATE TABLE ai_reviews (
    id                      UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    candidate_id            UUID            NOT NULL REFERENCES candidates (id) ON DELETE CASCADE,
    job_id                  UUID            NOT NULL REFERENCES jobs (id) ON DELETE CASCADE,
    match_score             NUMERIC(5,2)    CHECK (match_score BETWEEN 0 AND 100),
    recommendation          ai_recommendation,
    skills_match            JSONB,          -- { matched: [], missing: [] }
    experience_fit          JSONB,
    education_fit           JSONB,
    certifications_fit      JSONB,
    visa_check              JSONB,
    location_check          JSONB,
    notice_check            JSONB,
    salary_check            JSONB,
    submission_readiness    NUMERIC(5,2)    CHECK (submission_readiness BETWEEN 0 AND 100),
    recruiter_summary       TEXT,
    strengths               TEXT,
    weaknesses              TEXT,
    risks                   TEXT,
    interview_questions     JSONB,
    full_report_json        JSONB,          -- raw AI response for audit
    reviewed_by             UUID            REFERENCES employees (id) ON DELETE SET NULL,
    ai_model_used           VARCHAR(100),
    created_at              TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_ai_reviews_candidate_id ON ai_reviews (candidate_id);
CREATE INDEX idx_ai_reviews_job_id       ON ai_reviews (job_id);
CREATE INDEX idx_ai_reviews_score        ON ai_reviews (match_score DESC);


-- ============================================================
-- 12. ACTIVITY LOG / TIMELINE
-- ============================================================
CREATE TABLE activity_log (
    id            UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_type   VARCHAR(50) NOT NULL
                              CHECK (entity_type IN (
                                  'candidate', 'job', 'submission', 'interview',
                                  'offer', 'onboarding', 'client', 'employee'
                              )),
    entity_id     UUID        NOT NULL,
    action        VARCHAR(100) NOT NULL,   -- e.g. 'status_changed', 'cv_uploaded'
    old_value     JSONB,
    new_value     JSONB,
    description   TEXT,
    performed_by  UUID        REFERENCES employees (id) ON DELETE SET NULL,
    performed_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_activity_entity       ON activity_log (entity_type, entity_id);
CREATE INDEX idx_activity_performed_at ON activity_log (performed_at DESC);
CREATE INDEX idx_activity_performed_by ON activity_log (performed_by);


-- ============================================================
-- 13. NOTES
-- ============================================================
CREATE TABLE notes (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_type VARCHAR(50) NOT NULL
                            CHECK (entity_type IN (
                                'candidate', 'job', 'submission', 'client'
                            )),
    entity_id   UUID        NOT NULL,
    content     TEXT        NOT NULL,
    is_pinned   BOOLEAN     NOT NULL DEFAULT FALSE,
    created_by  UUID        REFERENCES employees (id) ON DELETE SET NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_notes_entity    ON notes (entity_type, entity_id);
CREATE INDEX idx_notes_is_pinned ON notes (is_pinned) WHERE is_pinned = TRUE;


-- ============================================================
-- 14. COMMUNICATIONS LOG
-- ============================================================
CREATE TABLE communications (
    id            UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_type   VARCHAR(50)     NOT NULL
                                  CHECK (entity_type IN ('candidate', 'client')),
    entity_id     UUID            NOT NULL,
    channel       message_channel NOT NULL,
    template_name VARCHAR(100),
    subject       VARCHAR(500),
    body          TEXT,
    sent_by       UUID            REFERENCES employees (id) ON DELETE SET NULL,
    sent_at       TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_communications_entity  ON communications (entity_type, entity_id);
CREATE INDEX idx_communications_sent_at ON communications (sent_at DESC);


-- ============================================================
-- 15. MESSAGE TEMPLATES
-- ============================================================
CREATE TABLE message_templates (
    id          UUID              PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100)      NOT NULL UNIQUE,
    subject     VARCHAR(500),
    body        TEXT              NOT NULL,
    channels    message_channel[] NOT NULL,
    is_active   BOOLEAN           NOT NULL DEFAULT TRUE,
    created_by  UUID              REFERENCES employees (id) ON DELETE SET NULL,
    created_at  TIMESTAMPTZ       NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ       NOT NULL DEFAULT NOW()
);

--=================================================================
    -- 16. REFRESH TOKEN
--=======================================================================
CREATE TABLE refresh_tokens (
    id              BIGSERIAL       PRIMARY KEY,
    token_id_hash   VARCHAR(100)    NOT NULL UNIQUE,
    user_id         VARCHAR(100)    NOT NULL,
    expires_at      TIMESTAMP       WITH TIME ZONE NOT NULL,
    revoked         BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP       WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_refresh_token_id ON refresh_tokens(token_id_hash);
CREATE INDEX idx_refresh_user_id ON refresh_tokens(user_id);

-- Seed default templates
INSERT INTO message_templates (name, subject, body, channels) VALUES
    ('Interview Invite',   'Interview Invitation',          'Dear {{candidate_name}}, we would like to invite you for an interview...', ARRAY['outlook','gmail','whatsapp']::message_channel[]),
    ('CV Request',         'CV Submission Request',         'Dear {{candidate_name}}, could you please send us your updated CV...', ARRAY['outlook','gmail','whatsapp']::message_channel[]),
    ('Follow-up',          'Following Up on Your Application','Dear {{candidate_name}}, we wanted to follow up on your application...', ARRAY['outlook','gmail','whatsapp']::message_channel[]),
    ('Offer Letter',       'Offer of Employment',           'Dear {{candidate_name}}, we are pleased to offer you the position of...', ARRAY['outlook','gmail']::message_channel[]),
    ('Joining Reminder',   'Joining Date Reminder',         'Dear {{candidate_name}}, this is a reminder that your joining date is...', ARRAY['outlook','gmail','whatsapp']::message_channel[]);


-- ============================================================
-- 16. updated_at TRIGGER  (applied to all mutable tables)
-- ============================================================
CREATE OR REPLACE FUNCTION fn_set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_clients_updated_at
    BEFORE UPDATE ON clients
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_employees_updated_at
    BEFORE UPDATE ON employees
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_candidates_updated_at
    BEFORE UPDATE ON candidates
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_jobs_updated_at
    BEFORE UPDATE ON jobs
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_submissions_updated_at
    BEFORE UPDATE ON submissions
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_interviews_updated_at
    BEFORE UPDATE ON interviews
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_offers_updated_at
    BEFORE UPDATE ON offers
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_onboarding_updated_at
    BEFORE UPDATE ON onboarding
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_notes_updated_at
    BEFORE UPDATE ON notes
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_message_templates_updated_at
    BEFORE UPDATE ON message_templates
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();

CREATE TRIGGER trg_communications_updated_at
    BEFORE UPDATE ON communications
    FOR EACH ROW EXECUTE FUNCTION fn_set_updated_at();


-- ============================================================
-- END OF SCHEMA
-- ============================================================
