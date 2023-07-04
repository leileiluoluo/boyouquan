package com.boyouquan.dao;

import com.boyouquan.model.BlogAggregate;
import com.boyouquan.model.BlogPost;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BlogPostDaoMapper {

    List<BlogAggregate> listBlogsOrderByPostDate(@Param("offset") int offset, @Param("rows") int rows);

    List<BlogPost> listLatestBlogPostsByAddress(String address, int offset, int rows);

    int countBlogPostsByAddress(String address);

    List<BlogPost> listBlogPosts(@Param("offset") int offset, @Param("rows") int rows);

    int countBlogPosts();

    void saveBlogPost(BlogPost blogPost);

    void deleteLaterBlogPostsByAddressAndDate(@Param("blogAddress") String blogAddress, @Param("datePoint") Date datePoint);

    Long countBlogs();

}
