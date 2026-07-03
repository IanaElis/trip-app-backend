CREATE TABLE users
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email          VARCHAR(255) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    username       VARCHAR(100) NOT NULL UNIQUE ,
    role           VARCHAR(30)  NOT NULL DEFAULT 'USER',
    status         VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at     TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE password_reset_token
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id    BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    token_hash VARCHAR(64) NOT NULL UNIQUE,
    expires_at TIMESTAMPTZ   NOT NULL,
    used_at    TIMESTAMPTZ,
    created_at TIMESTAMPTZ   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_password_reset_user ON password_reset_token(user_id);

CREATE TABLE failed_login_attempt
(
    email         VARCHAR(255) PRIMARY KEY,
    attempts      INTEGER   NOT NULL DEFAULT 0,
    first_attempt TIMESTAMPTZ NOT NULL,
    last_attempt  TIMESTAMPTZ NOT NULL
);

CREATE TABLE refresh_token
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    token      VARCHAR(200),
    user_id    BIGINT     NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    expires_at TIMESTAMPTZ NOT NULL,
    revoked    BOOLEAN    NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);


