-- insert admin
INSERT INTO users (id, email, password)
VALUES ('17630f9b-7fdc-4c5c-be71-af9398fa1ebd', 'admin@mail.com', '$2a$10$A6PIvXAesiFdcAHwUL/hmOb.i4bVsbZOeyxn7gUjgevYgYhiUa0EO'); -- admin admin

INSERT INTO users_roles (user_id, role_id)
VALUES ('17630f9b-7fdc-4c5c-be71-af9398fa1ebd', 2);

-- insert customers

INSERT INTO customer (id, name, surname, credit_limit, used_credit_limit)
VALUES
('2b201da2-32cf-4d11-93d2-7336bb353cba', 'name', 'surname', 100000.00, 0.00),
('bc0cfb62-fedd-4863-9c1e-d57207b417af', 'nametwo', 'surnametwo', 25000.00, 0.00);

INSERT INTO users (id, email, password)
VALUES
('67015373-18d0-4e94-8133-e1e00d95c9c8', 'customer@mail.com', '$2a$10$mQFstQIblFVYMLAM1wqDfOQuzT/6nuNMU/01jQpCumv5o5WDkrnai'),
('0aed1e22-8472-417f-a74f-40b861f90e0a', 'customer2@mail.com', '$2a$10$BLBDTOH5SvSvHvznFNgVueeqL9f4mBzeGY/jDsiRThPoh4uIUDxUG');

INSERT INTO users_roles (user_id, role_id)
VALUES
('67015373-18d0-4e94-8133-e1e00d95c9c8', 1),
('0aed1e22-8472-417f-a74f-40b861f90e0a', 1);


INSERT INTO user_customer (user_id, customer_id)
VALUES
('67015373-18d0-4e94-8133-e1e00d95c9c8', '2b201da2-32cf-4d11-93d2-7336bb353cba'),
('0aed1e22-8472-417f-a74f-40b861f90e0a', 'bc0cfb62-fedd-4863-9c1e-d57207b417af');


-- insert loans
INSERT INTO loan (id, customer_id, loan_amount, number_of_installment, create_date, is_paid)
VALUES
  ('454102b4-c869-40a6-b743-840804de96a1', '2b201da2-32cf-4d11-93d2-7336bb353cba', 5500.00, 6, '2024-12-31 12:30:37.571145', FALSE);

-- insert loan installments
INSERT INTO loan_installment (id, loan_id, amount, paid_amount, due_date, payment_date, is_paid)
VALUES
  ('2c62c4a4-9401-4695-b79b-7ffa184dac5d', '454102b4-c869-40a6-b743-840804de96a1', 916.67, 0.00, '2025-01-01 00:00:00', NULL, FALSE),
  ('b2fc2a1e-30aa-4ac2-b894-c2e1df53004a', '454102b4-c869-40a6-b743-840804de96a1', 916.67, 0.00, '2025-02-01 00:00:00', NULL, FALSE),
  ('643b3d70-44a2-4f5e-88b2-574aef8c9a45', '454102b4-c869-40a6-b743-840804de96a1', 916.67, 0.00, '2025-03-01 00:00:00', NULL, FALSE),
  ('00260abd-a674-4ce2-991c-8024e938cc4d', '454102b4-c869-40a6-b743-840804de96a1', 916.67, 0.00, '2025-04-01 00:00:00', NULL, FALSE),
  ('b2ad298c-0e09-4ad5-b5b5-143e24d7a47d', '454102b4-c869-40a6-b743-840804de96a1', 916.67, 0.00, '2025-05-01 00:00:00', NULL, FALSE),
  ('22cb7249-edf8-4414-a13b-a7ac99bcf23d', '454102b4-c869-40a6-b743-840804de96a1', 916.67, 0.00, '2025-06-01 00:00:00', NULL, FALSE);

