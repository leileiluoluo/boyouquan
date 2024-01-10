DROP TABLE IF EXISTS email_log;
CREATE TABLE email_log (
    blog_domain_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    type ENUM('blog_can_not_be_accessed') NOT NULL,
    sent_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00'
);