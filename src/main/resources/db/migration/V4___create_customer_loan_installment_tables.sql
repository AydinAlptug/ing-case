CREATE TABLE customer (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    credit_limit DECIMAL(15, 2) NOT NULL,
    used_credit_limit DECIMAL(15, 2) DEFAULT 0 NOT NULL
);

CREATE TABLE loan (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    loan_amount DECIMAL(15, 2) NOT NULL,
    number_of_installment INT NOT NULL,
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_paid BOOLEAN DEFAULT FALSE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
);

CREATE TABLE loan_installment (
    id UUID PRIMARY KEY,
    loan_id UUID NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    paid_amount DECIMAL(15, 2) DEFAULT 0 NOT NULL,
    due_date TIMESTAMP NOT NULL,
    payment_date TIMESTAMP,
    is_paid BOOLEAN DEFAULT FALSE NOT NULL,
    FOREIGN KEY (loan_id) REFERENCES loan(id) ON DELETE CASCADE
);
