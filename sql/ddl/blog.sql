DROP TABLE IF EXISTS blog;
CREATE TABLE blog (
    domain_name VARCHAR(100) NOT NULL PRIMARY KEY,
    admin_email VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL,
    rss_address VARCHAR(200) NOT NULL,
    description VARCHAR(300) NOT NULL,
    self_submitted BOOLEAN DEFAULT FALSE,
    collected_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    valid BOOLEAN DEFAULT TRUE,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_blog_domain_name ON blog (domain_name);