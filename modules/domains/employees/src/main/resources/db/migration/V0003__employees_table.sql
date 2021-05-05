CREATE TABLE employees
(
    id              VARCHAR(36) NOT NULL,
    version         INTEGER     NOT NULL,
    data            TEXT        NOT NULL,
    created_utc     VARCHAR(30) NOT NULL,
    last_update_utc VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
);
