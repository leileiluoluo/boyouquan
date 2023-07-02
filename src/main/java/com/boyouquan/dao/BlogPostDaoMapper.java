package com.boyouquan.dao;

import com.boyouquan.model.BlogPost;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BlogPostDaoMapper {

    List<BlogPost> listBlogPosts(@Param("offset") int offset, @Param("rows") int rows);

    int countBlogPosts();

    void saveBlogPost(BlogPost blogPost);

    void deleteLaterBlogPostsByAddressAndDate(@Param("blogAddress") String blogAddress, @Param("datePoint") Date datePoint);

    Long countBlogs();

}
