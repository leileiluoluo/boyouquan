DROP TABLE IF EXISTS access;
CREATE TABLE access (
    year_month_str VARCHAR(10) NOT NULL,
    blog_domain_name VARCHAR(100),
    link VARCHAR(300) NOT NULL,
    amount INT NOT NULL DEFAULT 0,
    PRIMARY KEY (year_month_str, blog_domain_name, link)
);

CREATE INDEX idx_access_year_month_str_domain_name ON access (year_month_str, blog_domain_name);