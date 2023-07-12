create table `blog_post` (
    blog_name varchar(100) not null,
    blog_address varchar(200) not null,
    title varchar(100) not null,
    description varchar(300) not null,
    link varchar(200) not null,
    created_at timestamp not null
);

create index blog_post_idx_address on `blog_post`(blog_address);