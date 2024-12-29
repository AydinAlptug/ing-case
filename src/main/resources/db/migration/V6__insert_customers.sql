INSERT INTO customer (id, name, surname, credit_limit, used_credit_limit)
VALUES
('2b201da2-32cf-4d11-93d2-7336bb353cba', 'name', 'surname', 5000.00, 0.00),
('bc0cfb62-fedd-4863-9c1e-d57207b417af', 'nametwo', 'surnametwo', 25000.00, 0.00);

INSERT INTO users (id, email, password)
VALUES
('67015373-18d0-4e94-8133-e1e00d95c9c8', 'customer', '$2a$10$mQFstQIblFVYMLAM1wqDfOQuzT/6nuNMU/01jQpCumv5o5WDkrnai'),
('0aed1e22-8472-417f-a74f-40b861f90e0a', 'customer2', '$2a$10$BLBDTOH5SvSvHvznFNgVueeqL9f4mBzeGY/jDsiRThPoh4uIUDxUG');

INSERT INTO users_roles (user_id, role_id)
VALUES
('67015373-18d0-4e94-8133-e1e00d95c9c8', 1),
('0aed1e22-8472-417f-a74f-40b861f90e0a', 1);


INSERT INTO user_customer (user_id, customer_id)
VALUES
('67015373-18d0-4e94-8133-e1e00d95c9c8', '2b201da2-32cf-4d11-93d2-7336bb353cba'),
('0aed1e22-8472-417f-a74f-40b861f90e0a', 'bc0cfb62-fedd-4863-9c1e-d57207b417af');
