package com.boyouquan.service;

import com.boyouquan.model.BlogAggregate;
import com.boyouquan.model.BlogPost;
import com.boyouquan.util.Pagination;

import java.util.Date;
import java.util.List;

public interface BlogPostService {

    BlogAggregate getBlogByRandom();

    Pagination<BlogAggregate> listBlogsOrderByPostDate(String keyword, int page, int size);

    Pagination<BlogPost> listLatestBlogPostsByAddress(String address, int page, int size);

    Pagination<BlogPost> listBlogPosts(String keyword, int page, int size);

    BlogPost getBlogByAddress(String address);

    void saveBlogPost(BlogPost blogPost);

    void deleteLaterBlogPostsByAddressAndDate(String address, Date datePoint);

    // FIXME
    List<BlogAggregate> listAllBlogs(String keyword);

    Long countBlogs(String keyword);

    int countPosts(String keyword);

}
