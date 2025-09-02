ALTER TABLE registered_learners ALTER COLUMN uid TYPE varchar USING uid::varchar(36);
