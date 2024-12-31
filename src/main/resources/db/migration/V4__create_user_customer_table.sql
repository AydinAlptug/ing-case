CREATE TABLE IF NOT EXISTS user_customer (
    user_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE,
    CONSTRAINT unique_customer UNIQUE (customer_id)
);
