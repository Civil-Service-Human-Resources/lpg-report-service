CREATE TABLE course_completion_events (
                                          event_id SERIAL,
                                          external_id VARCHAR(100) NOT NULL,
                                          user_id VARCHAR(100),
                                          user_email VARCHAR(150),
                                          course_id VARCHAR(100) NOT NULL,
                                          course_title VARCHAR(255) NOT NULL,
                                          event_timestamp TIMESTAMP NOT NULL,
                                          organisation_id INT NOT NULL,
                                          profession_id INT NOT NULL,
                                          grade_id INT
) PARTITION BY RANGE (event_timestamp);
