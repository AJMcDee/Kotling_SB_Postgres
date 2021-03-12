CREATE TABLE accounts
(
    id SERIAL PRIMARY KEY NOT NULL,
    fullname varchar(100),
    iban varchar(100),
    password varchar(100),
    token varchar(100),
    balance DECIMAL
);