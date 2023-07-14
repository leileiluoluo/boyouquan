package com.boyouquan.dao;

import com.boyouquan.model.BlogAggregate;
import com.boyouquan.model.BlogPost;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BlogPostDaoMapper {

    BlogAggregate getMostUpdatedBlogByLatestMonth();

    BlogAggregate getBlogByRandom();

    List<BlogAggregate> listBlogsOrderByPostDate(String keyword, @Param("offset") int offset, @Param("rows") int rows);

    List<BlogPost> listLatestBlogPostsByAddress(String address, int offset, int rows);

    int countBlogPostsByAddress(String address);

    List<BlogPost> listBlogPosts(String keyword, @Param("offset") int offset, @Param("rows") int rows);

    BlogAggregate getBlogByAddress(String address);

    BlogAggregate getBlogByDomain(String domain);

    BlogPost getBlogByLink(String link);

    int countBlogPosts(String keyword);

    void saveBlogPost(BlogPost blogPost);

    void deleteLaterBlogPostsByAddressAndDate(@Param("blogAddress") String blogAddress, @Param("datePoint") Date datePoint);

    List<BlogAggregate> listAllBlogs(String keyword);

    Long countBlogs(String keyword);

}
