CREATE TABLE roles
(
    id                  SERIAL PRIMARY KEY,
    name             VARCHAR(20)
);

INSERT INTO roles (name) VALUES ('ROLE_CUSTOMER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

CREATE TABLE users_roles (
                             user_id UUID,
                             role_id INT,
                             PRIMARY KEY (user_id, role_id),
                             FOREIGN KEY (user_id) REFERENCES users(id),
                             FOREIGN KEY (role_id) REFERENCES roles(id),
                             UNIQUE (user_id, role_id)
);