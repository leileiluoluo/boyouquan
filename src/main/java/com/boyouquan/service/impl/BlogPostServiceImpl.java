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
    public BlogAggregate getMostUpdatedBlogByLatestMonth() {
        return blogPostDaoMapper.getMostUpdatedBlogByLatestMonth();
    }

    @Override
    public BlogAggregate getBlogByRandom() {
        return blogPostDaoMapper.getBlogByRandom();
    }

    @Override
    public Pagination<BlogAggregate> listBlogsOrderByPostDate(String keyword, int page, int size) {
        if (page < 1 || size <= 0) {
            return Pagination.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<BlogAggregate> blogAggregates = blogPostDaoMapper.listBlogsOrderByPostDate(keyword, offset, size);
        int total = blogPostDaoMapper.countBlogs(keyword).intValue();
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
    public Pagination<BlogPost> listBlogPosts(String keyword, int page, int size) {
        if (page < 1 || size <= 0) {
            return Pagination.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<BlogPost> blogPosts = blogPostDaoMapper.listBlogPosts(keyword, offset, size);
        int total = blogPostDaoMapper.countBlogPosts(keyword);
        return Pagination.<BlogPost>builder().pageNo(page).pageSize(size).total(total).results(blogPosts);
    }

    @Override
    public BlogPost getBlogByAddress(String address) {
        return blogPostDaoMapper.getBlogByAddress(address);
    }

    @Override
    public BlogPost getBlogByLink(String link) {
        return blogPostDaoMapper.getBlogByLink(link);
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
    public List<BlogAggregate> listAllBlogs(String keyword) {
        return blogPostDaoMapper.listAllBlogs(keyword);
    }

    @Override
    public Long countBlogs(String keyword) {
        return blogPostDaoMapper.countBlogs(keyword);
    }

    @Override
    public int countPosts(String keyword) {
        return blogPostDaoMapper.countBlogPosts(keyword);
    }

}
