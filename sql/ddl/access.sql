DROP TABLE IF EXISTS access;
CREATE TABLE access (
    link VARCHAR(300) NOT NULL,
    blog_domain_name VARCHAR(100) NOT NULL,
    ip VARCHAR(100) NOT NULL,
    `from` ENUM('website', 'feed', 'unknown') NOT NULL DEFAULT 'unknown',
    accessed_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00'
);

CREATE INDEX idx_access_domain_name ON access (blog_domain_name);