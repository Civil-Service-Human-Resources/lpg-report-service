DELETE FROM registered_learners_report_requests;

ALTER TABLE registered_learners_report_requests
    ADD COLUMN requester_timezone VARCHAR(50);

ALTER TABLE registered_learners ALTER COLUMN created_timestamp TYPE timestamp USING created_timestamp::timestamp;
ALTER TABLE registered_learners ALTER COLUMN updated_timestamp TYPE timestamp USING updated_timestamp::timestamp;
