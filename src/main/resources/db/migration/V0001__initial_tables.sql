CREATE TABLE skills
(
    id    VARCHAR(36 CHAR)  NOT NULL,
    label VARCHAR(255 CHAR) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE projects
(
    id          VARCHAR(36 CHAR)  NOT NULL,
    label       VARCHAR(255 CHAR) NOT NULL,
    description CLOB              NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE employees
(
    id         VARCHAR(36 CHAR)  NOT NULL,
    first_name VARCHAR(255 CHAR) NOT NULL,
    last_name  VARCHAR(255 CHAR) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE employee_skills
(
    employee_id VARCHAR(36 CHAR) NOT NULL,
    skill_id    VARCHAR(36 CHAR) NOT NULL,
    level       INTEGER          NOT NULL,
    PRIMARY KEY (employee_id, skill_id),
    FOREIGN KEY (employee_id) references employees (ID),
    FOREIGN KEY (skill_id) references skills (ID)
);

CREATE TABLE employee_project_assignments
(
    id           VARCHAR(36 CHAR) NOT NULL,
    employee_id  VARCHAR(36 CHAR) NOT NULL,
    project_id   VARCHAR(36 CHAR) NOT NULL,
    contribution CLOB             NOT NULL,
    start_date   VARCHAR(10 CHAR) NOT NULL,
    end_date     VARCHAR(10 CHAR),
    PRIMARY KEY (employee_id, project_id),
    FOREIGN KEY (employee_id) references employees (ID),
    FOREIGN KEY (project_id) references projects (ID)
);
