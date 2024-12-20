DROP TABLE IF EXISTS user;
CREATE TABLE user (
    username VARCHAR(20) NOT NULL,
    md5password VARCHAR(50) NOT NULL,
    role VARCHAR(10) NOT NULL,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_user_username ON user (username);