DROP TABLE company IF EXISTS;

CREATE TABLE company  (
    id BIGINT auto_increment NOT NULL PRIMARY KEY,
    name VARCHAR(20)
);

DROP TABLE console IF EXISTS;

CREATE TABLE console  (
    id BIGINT auto_increment NOT NULL PRIMARY KEY,
    name VARCHAR(20),
    company_id BIGINT,
    FOREIGN KEY(company_id) REFERENCES company(id)
);

DROP TABLE score IF EXISTS;

CREATE TABLE score  (
    id BIGINT auto_increment NOT NULL PRIMARY KEY,
    name VARCHAR(100),
    metascore INT,
    console_id BIGINT,
    userscore FLOAT(8),
    date date,
    FOREIGN KEY(console_id) REFERENCES console(id)
);