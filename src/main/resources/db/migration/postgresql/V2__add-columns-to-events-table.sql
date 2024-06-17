ALTER TABLE course_completion_events
    ADD COLUMN grade_code VARCHAR(100),
    ADD COLUMN profession_name VARCHAR(255),
    ADD COLUMN organisation_abbreviation VARCHAR(255);