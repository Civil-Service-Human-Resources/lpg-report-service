ALTER TABLE course_completion_report_requests
    ADD COLUMN url_slug VARCHAR(20) NOT NULL DEFAULT substr(md5(random()::text), 1, 20) UNIQUE,
    ADD COLUMN download_base_url VARCHAR(255) NOT NULL DEFAULT 'NULL',
    ADD COLUMN times_downloaded INT NOT NULL DEFAULT 0;
