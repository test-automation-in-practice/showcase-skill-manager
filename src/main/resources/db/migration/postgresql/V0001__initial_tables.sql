CREATE TABLE skills
(
    id       VARCHAR(36) NOT NULL,
    version  INTEGER     NOT NULL,
    data     TEXT        NOT NULL,
    keywords TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE projects
(
    id       VARCHAR(36) NOT NULL,
    version  INTEGER     NOT NULL,
    data     TEXT        NOT NULL,
    keywords TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE employees
(
    id          VARCHAR(36) NOT NULL,
    version     INTEGER     NOT NULL,
    data        TEXT        NOT NULL,
    skill_ids   TEXT,
    project_ids TEXT,
    PRIMARY KEY (id)
);
