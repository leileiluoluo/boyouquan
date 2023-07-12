create table `blog_access` (
    blog_address varchar(200) not null,
    link varchar(200) not null,
    ip varchar(100) not null,
    accessed_at timestamp not null
);

create index blog_access_idx_link on `blog_access`(link);
