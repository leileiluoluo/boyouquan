package com.boyouquan.service;

import com.boyouquan.model.BlogAggregate;
import com.boyouquan.model.BlogPost;
import com.boyouquan.util.Pagination;

import java.util.Date;

public interface BlogPostService {

    Pagination<BlogAggregate> listBlogsOrderByPostDate(int page, int size);

    Pagination<BlogPost> listLatestBlogPostsByAddress(String address, int page, int size);

    Pagination<BlogPost> listBlogPosts(int page, int size);

    void saveBlogPost(BlogPost blogPost);

    void deleteLaterBlogPostsByAddressAndDate(String address, Date datePoint);

    Long countBlogs();

    int countPosts();

}
