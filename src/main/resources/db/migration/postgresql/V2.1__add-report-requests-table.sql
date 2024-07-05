CREATE TABLE report_requests_status (
    status VARCHAR(20) PRIMARY KEY
);

INSERT INTO report_requests_status (status) VALUES
    ('REQUESTED'),
    ('PROCESSING'),
    ('SUCCESS'),
    ('FAILED');

CREATE TABLE course_completion_report_requests (
    report_request_id SERIAL,
    requester_id VARCHAR(36),
    requester_email VARCHAR(100),
    requested_timestamp TIMESTAMPTZ NOT NULL DEFAULT now(),
    completed_timestamp TIMESTAMPTZ,
    status VARCHAR(20) NOT NULL,
    from_date TIMESTAMPTZ NOT NULL,
    to_date TIMESTAMPTZ NOT NULL,
    course_ids text[] NOT NULL,
    organisation_ids INT[] NOT NULL,
    profession_ids INT[],
    grade_ids INT[],

    CONSTRAINT status_fk
        FOREIGN KEY(status)
            REFERENCES report_requests_status(status)
);