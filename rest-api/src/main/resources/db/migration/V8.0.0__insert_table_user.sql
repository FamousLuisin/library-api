INSERT INTO tb_users (
    user_name,
    full_name,
    password,
    account_non_expired,
    account_non_locked,
    credentials_non_expired,
    enabled
) VALUES
(
    'nok',
    'Nok admin123',
    '538634e98538a5c05b0b328159a04b3225114b251f7ab9ba3ec174e911f0abed7481d61285ac194e',
    TRUE,
    TRUE,
    TRUE,
    TRUE
),
(
    'noc',
    'Noc admin234',
    'fdb35bf35d56e253cb4f8454fc1919c944dd22a8df199ca561322cc5cef54bbfe09329284a1713cf',
    TRUE,
    TRUE,
    TRUE,
    TRUE
);