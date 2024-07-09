CREATE TABLE IF NOT EXISTS course_completion_events_2024_1_1 PARTITION OF course_completion_events FOR VALUES FROM ('2024-01-01') TO ('2024-01-02');
INSERT INTO course_completion_events (external_id, user_id, organisation_id, profession_id, grade_id, user_email, course_id, course_title, event_timestamp)
VALUES
    ('MESSAGEID0', 'user1', 1, 2, 3, 'email', 'c1', 'c1', '2024-01-01 09:20:00'),
    ('MESSAGEID1', 'user1', 1, 2, 3, 'email', 'c2', 'c2', '2024-01-01 10:40:00'),
    ('MESSAGEID2', 'user2', 1, 4, 3, 'email', 'c1', 'c1', '2024-01-01 23:15:00'),
    ('MESSAGEID3', 'user2', 1, 4, 3, 'email', 'c2', 'c2', '2024-01-01 18:09:00'),
    ('MESSAGEID4', 'user3', 1, 2, 2, 'email', 'c1', 'c1', '2024-01-01 09:30:00'),
    ('MESSAGEID5', 'user4', 1, 1, 3, 'email', 'c1', 'c1', '2024-01-01 19:30:00');

CREATE TABLE IF NOT EXISTS course_completion_events_2024_2_1 PARTITION OF course_completion_events FOR VALUES FROM ('2024-02-01') TO ('2024-02-02');
CREATE TABLE IF NOT EXISTS course_completion_events_2024_2_2 PARTITION OF course_completion_events FOR VALUES FROM ('2024-02-02') TO ('2024-02-03');
CREATE TABLE IF NOT EXISTS course_completion_events_2024_2_3 PARTITION OF course_completion_events FOR VALUES FROM ('2024-02-03') TO ('2024-02-04');
INSERT INTO course_completion_events (external_id, user_id, organisation_id, profession_id, grade_id, user_email, course_id, course_title, event_timestamp)
VALUES
    ('MESSAGEID4', 'user3', 1, 2, 2, 'email', 'c2', 'c2', '2024-02-01 10:30:00'),
    ('MESSAGEID5', 'user4', 1, 1, 3, 'email', 'c2', 'c2', '2024-02-01 19:30:00'),
    ('MESSAGEID5', 'user5', 2, 2, 3, 'email', 'c1', 'c1', '2024-02-02 14:30:00'),
    ('MESSAGEID5', 'user4', 1, 1, 3, 'email', 'c3', 'c3', '2024-02-02 09:33:00'),
    ('MESSAGEID5', 'user4', 1, 1, 3, 'email', 'c5', 'c5', '2024-02-03 06:10:00');

CREATE TABLE IF NOT EXISTS course_completion_events_2024_3_1 PARTITION OF course_completion_events FOR VALUES FROM ('2024-03-01') TO ('2024-03-02');
CREATE TABLE IF NOT EXISTS course_completion_events_2024_3_4 PARTITION OF course_completion_events FOR VALUES FROM ('2024-03-04') TO ('2024-03-05');
CREATE TABLE IF NOT EXISTS course_completion_events_2024_3_10 PARTITION OF course_completion_events FOR VALUES FROM ('2024-03-10') TO ('2024-03-11');
CREATE TABLE IF NOT EXISTS course_completion_events_2024_3_20 PARTITION OF course_completion_events FOR VALUES FROM ('2024-03-20') TO ('2024-03-21');
INSERT INTO course_completion_events (external_id, user_id, organisation_id, profession_id, grade_id, user_email, course_id, course_title, event_timestamp)
VALUES
    ('MESSAGEID4', 'user3', 1, 2, 2, 'email', 'c2', 'c2', '2024-03-01 10:30:00'),
    ('MESSAGEID5', 'user5', 2, 2, 3, 'email', 'c2', 'c2', '2024-03-04 14:30:00'),
    ('MESSAGEID5', 'user4', 1, 1, 3, 'email', 'c5', 'c5', '2024-03-10 09:33:00'),
    ('MESSAGEID5', 'user2', 1, 4, 3, 'email', 'c5', 'c5', '2024-03-10 15:53:00'),
    ('MESSAGEID5', 'user2', 1, 4, 3, 'email', 'c4', 'c4', '2024-03-20 18:10:00'),
    ('MESSAGEID5', 'user1', 1, 2, 3, 'email', 'c5', 'c5', '2024-03-20 17:10:00');

CREATE TABLE IF NOT EXISTS course_completion_events_2024_6_1 PARTITION OF course_completion_events FOR VALUES FROM ('2024-06-01') TO ('2024-06-02');
CREATE TABLE IF NOT EXISTS course_completion_events_2024_6_2 PARTITION OF course_completion_events FOR VALUES FROM ('2024-06-02') TO ('2024-06-03');
INSERT INTO course_completion_events (external_id, user_id, organisation_id, profession_id, grade_id, user_email, course_id, course_title, event_timestamp)
VALUES
    ('MESSAGEID4', 'user3', 1, 2, 2, 'email', 'c5', 'c5', '2024-06-01 07:20:00'),
    ('MESSAGEID5', 'user6', 3, 2, 2, 'email', 'c1', 'c1', '2024-06-02 09:44:00'),
    ('MESSAGEID5', 'user6', 3, 2, 2, 'email', 'c5', 'c5', '2024-06-02 10:19:00'),
    ('MESSAGEID5', 'user6', 3, 2, 2, 'email', 'c4', 'c4', '2024-06-02 11:13:00');


