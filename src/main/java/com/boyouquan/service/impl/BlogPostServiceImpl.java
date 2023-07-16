package com.boyouquan.service.impl;

import com.boyouquan.dao.BlogPostDaoMapper;
import com.boyouquan.model.BlogAggregate;
import com.boyouquan.model.BlogPost;
import com.boyouquan.service.BlogPostService;
import com.boyouquan.util.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    private final Logger logger = LoggerFactory.getLogger(BlogPostServiceImpl.class);

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
    public BlogAggregate getBlogByAddress(String address) {
        return blogPostDaoMapper.getBlogByAddress(address);
    }

    @Override
    public BlogAggregate getBlogByDomain(String domain) {
        return blogPostDaoMapper.getBlogByDomain(domain);
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
            logger.error(e.getMessage(), e);
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
