USE learner_record;

ALTER TABLE booking MODIFY `cancellation_reason` enum('PAYMENT', 'REQUESTED', 'BEREAVEMENT', 'ILLNESS', 'PRIORITIES') DEFAULT NULL;