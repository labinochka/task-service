-- Создание таблицы user
CREATE TABLE client
(
    id       UUID PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL,
    CONSTRAINT user_role_enum_check CHECK (role IN ('ADMIN', 'USER'))
);

-- Создание таблицы task
CREATE TABLE task
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    status      VARCHAR(50)  NOT NULL,
    priority    VARCHAR(50)  NOT NULL,
    author_id   UUID         NOT NULL,
    executor_id UUID,
    created_at  TIMESTAMPTZ  NOT NULL,
    updated_at  TIMESTAMPTZ,
    CONSTRAINT fk_task_author FOREIGN KEY (author_id) REFERENCES client (id),
    CONSTRAINT fk_task_executor FOREIGN KEY (executor_id) REFERENCES client (id),
    CONSTRAINT task_status_enum_check CHECK (status IN ('FOR_EXECUTION', 'IN_DEVELOPMENT', 'REVIEW',
                                                        'READY_FOR_TESTING', 'IN_TESTING', 'DONE')),
    CONSTRAINT task_priority_enum_check CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH'))
);

-- Создание таблицы comment
CREATE TABLE comment
(
    id         UUID PRIMARY KEY,
    comment    VARCHAR(255) NOT NULL,
    task_id    UUID         NOT NULL,
    author_id  UUID         NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ,
    CONSTRAINT fk_comment_task FOREIGN KEY (task_id) REFERENCES task (id),
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES client (id)
);