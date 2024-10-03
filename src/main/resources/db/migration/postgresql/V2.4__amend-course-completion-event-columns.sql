ALTER TABLE course_completion_events
    RENAME COLUMN grade_code TO grade_name;

ALTER TABLE course_completion_events
    RENAME COLUMN organisation_abbreviation TO organisation_name;
