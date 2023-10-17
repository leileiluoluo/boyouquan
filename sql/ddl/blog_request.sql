DROP TABLE IF EXISTS blog_request;
CREATE TABLE blog_request (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rss_address VARCHAR(200) NOT NULL,
    admin_email VARCHAR(100) NOT NULL,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(300) NOT NULL,
    self_submitted BOOLEAN DEFAULT FALSE,
    requested_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    updated_at TIMESTAMP NOT NULL DEFAULT '2023-07-01 00:00:00',
    status ENUM('submitted', 'system_check_valid', 'system_check_invalid', 'approved', 'rejected') DEFAULT NULL,
    reason VARCHAR(300),
    deleted BOOLEAN DEFAULT FALSE
);