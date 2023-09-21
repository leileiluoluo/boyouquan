DROP TABLE IF EXISTS post;
CREATE TABLE post (
    link VARCHAR(300) NOT NULL PRIMARY KEY,
    blog_domain_name VARCHAR(100) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    published_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    draft BOOLEAN DEFAULT FALSE,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_post_domain_name ON post (blog_domain_name);