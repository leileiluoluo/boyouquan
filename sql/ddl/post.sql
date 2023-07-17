DROP TABLE IF EXISTS post;
CREATE TABLE post (
    link VARCHAR(200) NOT NULL PRIMARY KEY,
    blog_domain_name VARCHAR(100) NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(300) NOT NULL,
    published_at TIMESTAMP NOT NULL,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_post_domain_name ON post (blog_domain_name);