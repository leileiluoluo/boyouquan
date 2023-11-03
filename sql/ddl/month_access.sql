DROP TABLE IF EXISTS monthly_access;
CREATE TABLE monthly_access (
    year_month_str VARCHAR(10) NOT NULL,
    blog_domain_name VARCHAR(100),
    link VARCHAR(300) NOT NULL,
    amount INT NOT NULL DEFAULT 0,
    PRIMARY KEY (year_month_str, blog_domain_name, link)
);

-- TODO, please add index later

-- FIXME, please remove me when data transferred
INSERT INTO monthly_access
    SELECT DATE_FORMAT(accessed_at, '%Y-%m'), blog_domain_name, link, COUNT(*)
    FROM access
    GROUP BY DATE_FORMAT(accessed_at, '%Y-%m'), blog_domain_name, link;