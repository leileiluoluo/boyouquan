DROP TABLE IF EXISTS planet_shuttle;
CREATE TABLE planet_shuttle (
    year_month_str VARCHAR(10) NOT NULL,
    blog_domain_name VARCHAR(100),
    to_blog_address VARCHAR(300) NOT NULL,
    amount INT NOT NULL DEFAULT 0,
    PRIMARY KEY (year_month_str, blog_domain_name, to_blog_address)
);

CREATE INDEX idx_planet_shuttle_blog_domain_name ON planet_shuttle (blog_domain_name);
CREATE INDEX idx_planet_shuttle_year_month_str_domain_name ON planet_shuttle (year_month_str, blog_domain_name);