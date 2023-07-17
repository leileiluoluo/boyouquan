package com.boyouquan.service.impl;

import com.boyouquan.dao.BlogDaoMapper;
import com.boyouquan.model.Blog;
import com.boyouquan.model.NewBlogInfo;
import com.boyouquan.model.Post;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.NewPagination;
import com.boyouquan.util.NewPaginationBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDaoMapper blogDaoMapper;
    @Autowired
    private PostService postService;

    @Override
    public List<Blog> listAll() {
        return blogDaoMapper.listAll();
    }

    @Override
    public NewBlogInfo getBlogInfoByDomainName(String domainName) {
        Blog blog = getByDomainName(domainName);
        if (null == blog) {
            return null;
        }

        // assembling
        // common fields
        NewBlogInfo blogInfo = new NewBlogInfo();
        BeanUtils.copyProperties(blog, blogInfo);

        // other fields
        // FIXME, call service
        blogInfo.setPostCount(10L);
        blogInfo.setAccessCount(10L);
        blogInfo.setLatestPublishedAt(new Date());

        return blogInfo;
    }

    @Override
    public NewPagination<NewBlogInfo> listBlogInfosWithKeyWord(String keyword, int page, int size) {
        if (page < 1 || size <= 0) {
            return NewPaginationBuilder.buildEmptyResults();
        }

        // list
        List<NewBlogInfo> blogInfos = new ArrayList<>();
        NewPagination<Blog> blogPagination = listWithKeyWord(keyword, page, size);
        for (Blog blog : blogPagination.getResults()) {
            NewBlogInfo blogInfo = new NewBlogInfo();
            BeanUtils.copyProperties(blog, blogInfo);

            // FIXME
            String blogDomainName = blog.getDomainName();
            Long count = postService.countByBlogDomainName(blogDomainName);
            blogInfo.setPostCount(count);
            Date latestUpdatedAt = postService.getLatestPublishedAtByBlogDomainName(blogDomainName);
            blogInfo.setAccessCount(0L);
            blogInfo.setLatestPublishedAt(latestUpdatedAt);

            List<Post> latestPosts = postService.listByBlogDomainName(blog.getDomainName(), 3);
            blogInfo.setLatestPosts(latestPosts);
            blogInfos.add(blogInfo);
        }
        long total = blogPagination.getTotal();
        return NewPaginationBuilder.<NewBlogInfo>newBuilder()
                .pageNo(page)
                .pageSize(size)
                .total(total)
                .results(blogInfos).build();
    }

    @Override
    public NewPagination<Blog> listWithKeyWord(String keyword, int page, int size) {
        if (page < 1 || size <= 0) {
            return NewPaginationBuilder.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<Blog> blogs = blogDaoMapper.listWithKeyWord(keyword, offset, size);
        Long total = blogDaoMapper.countWithKeyword(keyword);
        return NewPaginationBuilder.<Blog>newBuilder()
                .pageNo(page)
                .pageSize(size)
                .total(total)
                .results(blogs).build();
    }

    @Override
    public boolean existsByDomainName(String domainName) {
        return blogDaoMapper.existsByDomainName(domainName);
    }

    @Override
    public Blog getByDomainName(String domainName) {
        return blogDaoMapper.getByDomainName(domainName);
    }

    @Override
    public void save(Blog blog) {
        blogDaoMapper.save(blog);
    }

    @Override
    public void update(Blog blog) {
        assert null != blog
                && StringUtils.isNotBlank(blog.getDomainName());

        blogDaoMapper.update(blog);
    }

    @Override
    public void deleteByDomainName(String domainName) {
        blogDaoMapper.deleteByDomainName(domainName);
    }

}
