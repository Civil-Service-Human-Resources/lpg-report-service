ALTER TABLE registered_learners_report_requests ALTER COLUMN requested_timestamp TYPE timestamp USING requested_timestamp::timestamp;
ALTER TABLE registered_learners_report_requests ALTER COLUMN completed_timestamp TYPE timestamp USING completed_timestamp::timestamp;
ALTER TABLE registered_learners_report_requests ALTER COLUMN last_downloaded_timestamp TYPE timestamp USING last_downloaded_timestamp::timestamp;

ALTER TABLE course_completion_report_requests ALTER COLUMN requested_timestamp TYPE timestamp USING requested_timestamp::timestamp;
ALTER TABLE course_completion_report_requests ALTER COLUMN completed_timestamp TYPE timestamp USING completed_timestamp::timestamp;
ALTER TABLE course_completion_report_requests ALTER COLUMN from_date TYPE timestamp USING from_date::timestamp;
ALTER TABLE course_completion_report_requests ALTER COLUMN to_date TYPE timestamp USING to_date::timestamp;
