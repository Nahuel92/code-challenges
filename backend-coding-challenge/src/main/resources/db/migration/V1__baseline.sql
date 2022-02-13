CREATE TABLE IF NOT EXISTS property_type
(
    id   INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS address
(
    id       INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,

    street   VARCHAR(50),

    city     VARCHAR(50),

    state    VARCHAR(50),

    zip      VARCHAR(50),

    timezone VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS property
(
    id           INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    rent_price   FLOAT NOT NULL,
    create_time  VARCHAR(50),
    type         INT UNSIGNED UNIQUE,
    rentPrice    FLOAT,
    address      INT UNSIGNED UNIQUE,
    emailAddress VARCHAR(50),
    code         VARCHAR(50),

    FOREIGN KEY (type) REFERENCES property_type (id),
    FOREIGN KEY (address) REFERENCES address (id)
);