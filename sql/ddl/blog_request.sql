DROP TABLE IF EXISTS blog_request;
CREATE TABLE blog_request (
    id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    rss_address VARCHAR(200) NOT NULL,
    admin_email VARCHAR(100) NOT NULL,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(300) NOT NULL,
    self_submitted BOOLEAN DEFAULT FALSE,
    requested_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    status ENUM('submitted', 'system_check_valid', 'system_check_invalid', 'approved', 'rejected') DEFAULT NULL,
    reason VARCHAR(300) NULL,
    deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_blog_request_rss_address ON blog (rss_address);