CREATE TABLE registered_learners(
    uid CHAR(36) PRIMARY KEY,
    email VARCHAR(150) NOT NULL,
    active BOOLEAN NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    organisation_id INT4,
    organisation_name TEXT,
    grade_id INT4,
    grade_name VARCHAR(255),
    profession_id INT4,
    profession_name VARCHAR(255),
    created_timestamp TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_timestamp TIMESTAMPTZ NOT NULL
);

CREATE TABLE registered_learners_report_requests (
    report_request_id SERIAL4 PRIMARY KEY,
    requester_id VARCHAR(36) NOT NULL,
    requester_email VARCHAR(150) NOT NULL,
    requester_full_name VARCHAR(255) NOT NULL,
    organisation_ids INT[],
    status VARCHAR(20) NOT NULL DEFAULT 'REQUESTED',
    url_slug VARCHAR(20) NOT NULL DEFAULT substr(md5(random()::text), 1, 20) UNIQUE,
    download_base_url VARCHAR(255) NOT NULL DEFAULT 'NULL',
    times_downloaded INT4 NOT NULL DEFAULT 0,
    requested_timestamp TIMESTAMPTZ NOT NULL DEFAULT now(),
    completed_timestamp TIMESTAMPTZ,
    last_downloaded_timestamp TIMESTAMPTZ,

    CONSTRAINT status_fk
        FOREIGN KEY(status)
            REFERENCES report_requests_status(status)
);