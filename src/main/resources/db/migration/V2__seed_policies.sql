-- Seed a representative spread across all regions and both statuses.
-- Dates are relative to CURRENT_DATE so the "expiring soon" rule (end within 30 days)
-- is exercised regardless of when the migration runs.
INSERT INTO policies
    (policy_number, holder_name, region, status, premium_amount, premium_currency, start_date, end_date)
VALUES
    ('POL-SG-100001', 'Jane Tan',       'SG', 'ACTIVE',  1250.00, 'SGD', CURRENT_DATE - 355, CURRENT_DATE + 10),
    ('POL-SG-100002', 'Marcus Lee',     'SG', 'ACTIVE',   980.50, 'SGD', CURRENT_DATE - 700, CURRENT_DATE + 400),
    ('POL-SG-100003', 'Siti Nurhaliza', 'SG', 'LAPSED',  1100.00, 'SGD', CURRENT_DATE - 1000, CURRENT_DATE - 200),
    ('POL-HK-200001', 'Wong Mei',       'HK', 'ACTIVE',  3200.00, 'HKD', CURRENT_DATE - 300, CURRENT_DATE + 25),
    ('POL-HK-200002', 'Chan Ka Ho',     'HK', 'LAPSED',  1500.00, 'HKD', CURRENT_DATE - 800, CURRENT_DATE - 30),
    ('POL-AU-300001', 'Olivia Smith',   'AU', 'ACTIVE',   540.00, 'AUD', CURRENT_DATE - 200, CURRENT_DATE + 700),
    ('POL-AU-300002', 'Liam Jones',     'AU', 'ACTIVE',   720.75, 'AUD', CURRENT_DATE - 100, CURRENT_DATE + 20),
    ('POL-IN-400001', 'Aarav Sharma',   'IN', 'ACTIVE', 15000.00, 'INR', CURRENT_DATE - 365, CURRENT_DATE + 400),
    ('POL-IN-400002', 'Priya Patel',    'IN', 'LAPSED',  8500.00, 'INR', CURRENT_DATE - 900, CURRENT_DATE - 100),
    ('POL-IN-400003', 'Rohan Mehta',    'IN', 'ACTIVE', 22000.00, 'INR', CURRENT_DATE - 120, CURRENT_DATE + 28),
    ('POL-JP-500001', 'Yuki Tanaka',    'JP', 'ACTIVE', 95000.00, 'JPY', CURRENT_DATE - 250, CURRENT_DATE + 5),
    ('POL-JP-500002', 'Haruto Sato',    'JP', 'ACTIVE', 120000.00, 'JPY', CURRENT_DATE - 50, CURRENT_DATE + 730);
