CREATE TABLE skills
(
    id   VARCHAR(36 CHAR)  NOT NULL,
    data CLOB NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE projects
(
    id   VARCHAR(36 CHAR)  NOT NULL,
    data CLOB NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE employees
(
    id          VARCHAR(36 CHAR)  NOT NULL,
    data        CLOB NOT NULL,
    skill_ids   CLOB,
    project_ids CLOB,
    PRIMARY KEY (id)
);

--CREATE INDEX idx_employee_skill_ids ON employees(skill_ids);
--CREATE INDEX idx_employee_project_ids ON employees(project_ids);
