package com.boyouquan.service.impl;

import com.boyouquan.dao.BlogPostDaoMapper;
import com.boyouquan.model.BlogAggregate;
import com.boyouquan.model.BlogPost;
import com.boyouquan.service.BlogPostService;
import com.boyouquan.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    @Autowired
    private BlogPostDaoMapper blogPostDaoMapper;

    @Override
    public Pagination<BlogAggregate> listBlogsOrderByPostDate(int page, int size) {
        if (page < 1 || size <= 0) {
            return Pagination.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<BlogAggregate> blogAggregates = blogPostDaoMapper.listBlogsOrderByPostDate(offset, size);
        int total = blogPostDaoMapper.countBlogs().intValue();
        return Pagination.<BlogAggregate>builder().pageNo(page).pageSize(size).total(total).results(blogAggregates);
    }

    @Override
    public Pagination<BlogPost> listLatestBlogPostsByAddress(String address, int page, int size) {
        if (page < 1 || size <= 0) {
            return Pagination.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<BlogPost> blogPosts = blogPostDaoMapper.listLatestBlogPostsByAddress(address, offset, size);
        int total = blogPostDaoMapper.countBlogPostsByAddress(address);
        return Pagination.<BlogPost>builder().pageNo(page).pageSize(size).total(total).results(blogPosts);
    }

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
    public void deleteLaterBlogPostsByAddressAndDate(String address, Date datePoint) {
        blogPostDaoMapper.deleteLaterBlogPostsByAddressAndDate(address, datePoint);
    }

    @Override
    public Long countBlogs() {
        return blogPostDaoMapper.countBlogs();
    }

    @Override
    public int countPosts() {
        return blogPostDaoMapper.countBlogPosts();
    }

}
