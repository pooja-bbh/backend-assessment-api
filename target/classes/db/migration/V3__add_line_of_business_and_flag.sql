-- Add line of business and review-flag columns.
-- line_of_business is added nullable, backfilled for existing rows, then made NOT NULL.
ALTER TABLE policies ADD COLUMN line_of_business VARCHAR(20);
ALTER TABLE policies ADD COLUMN flagged_for_review BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE policies SET line_of_business = CASE policy_number
    WHEN 'POL-SG-100001' THEN 'LIFE'
    WHEN 'POL-SG-100002' THEN 'HEALTH'
    WHEN 'POL-SG-100003' THEN 'MOTOR'
    WHEN 'POL-HK-200001' THEN 'PROPERTY'
    WHEN 'POL-HK-200002' THEN 'TRAVEL'
    WHEN 'POL-AU-300001' THEN 'MARINE'
    WHEN 'POL-AU-300002' THEN 'LIABILITY'
    WHEN 'POL-IN-400001' THEN 'LIFE'
    WHEN 'POL-IN-400002' THEN 'HEALTH'
    WHEN 'POL-IN-400003' THEN 'MOTOR'
    WHEN 'POL-JP-500001' THEN 'PROPERTY'
    WHEN 'POL-JP-500002' THEN 'TRAVEL'
END;

ALTER TABLE policies ALTER COLUMN line_of_business SET NOT NULL;
