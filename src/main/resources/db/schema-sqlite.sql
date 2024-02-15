CREATE TABLE IF NOT EXISTS municipality (
    code varchar(255) not null PRIMARY KEY,
    name varchar(255) not null
);

CREATE TABLE IF NOT EXISTS municipality_part (
    code varchar(255) not null PRIMARY KEY,
    name varchar(255) not null,
    municipality_id varchar(255) not null,
    FOREIGN KEY(municipality_id) REFERENCES municipality(code) ON DELETE CASCADE
);