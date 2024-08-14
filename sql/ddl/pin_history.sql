DROP TABLE IF EXISTS pin_history;
CREATE TABLE pin_history (
    blog_domain_name VARCHAR(100),
    link VARCHAR(300) NOT NULL,
    pinned_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    PRIMARY KEY (blog_domain_name, link)
);
