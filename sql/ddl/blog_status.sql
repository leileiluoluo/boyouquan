DROP TABLE IF EXISTS blog_status;
CREATE TABLE blog_status (
    blog_domain_name VARCHAR(100) NOT NULL,
    status ENUM('ok', 'timeout', 'can_not_be_accessed') NOT NULL,
    code INT NOT NULL,
    reason VARCHAR(300) NULL,
    detected_at TIMESTAMP NOT NULL,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_blog_status_domain_name ON blog_status (blog_domain_name);