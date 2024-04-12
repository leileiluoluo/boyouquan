DROP TABLE IF EXISTS blog_location;
CREATE TABLE blog_location (
    domain_name VARCHAR(100) PRIMARY KEY,
    location VARCHAR(100) NOT NULL,
    isp VARCHAR(200) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    updated_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_blog_location_domain_name ON blog_location (domain_name);