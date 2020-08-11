CREATE TABLE skills
(
    id       VARCHAR(36 CHAR) NOT NULL,
    version  INT NOT NULL,
    data     CLOB NOT NULL,
    keywords CLOB,
    PRIMARY KEY (id)
);

CREATE TABLE projects
(
    id       VARCHAR(36 CHAR) NOT NULL,
    version  INT NOT NULL,
    data     CLOB NOT NULL,
    keywords CLOB,
    PRIMARY KEY (id)
);

CREATE TABLE employees
(
    id          VARCHAR(36 CHAR) NOT NULL,
    version     INT NOT NULL,
    data        CLOB NOT NULL,
    skill_ids   CLOB,
    project_ids CLOB,
    PRIMARY KEY (id)
);
