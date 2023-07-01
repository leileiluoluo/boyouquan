package com.boyouquan.service.impl;

import com.boyouquan.dao.BlogPostDaoMapper;
import com.boyouquan.model.BlogPost;
import com.boyouquan.service.BlogPostService;
import com.boyouquan.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    @Autowired
    private BlogPostDaoMapper blogPostDaoMapper;

    @Override
    public Pagination<BlogPost> listBlogPosts(int page, int size) {
        if (page < 1 || size <= 0) {
            return Pagination.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<BlogPost> blogPosts = blogPostDaoMapper.listBlogPosts(offset, size);
        int total = blogPostDaoMapper.countBlogPosts();
        return Pagination.<BlogPost>builder().pageNo(page).pageSize(size).total(total).results(blogPosts);
    }

    @Override
    public void saveBlogPost(BlogPost blogPost) {
        try {
            blogPostDaoMapper.saveBlogPost(blogPost);
        } catch (Exception e) {
            System.out.printf("error occurred: %s\n", e.getMessage());
        }
    }

    @Override
    public void deleteBlogPostByBlogAddress(String blogAddress) {
        blogPostDaoMapper.deleteBlogPostByBlogAddress(blogAddress);
    }

}
