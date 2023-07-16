package com.boyouquan.service.impl;

import com.boyouquan.dao.BlogDaoMapper;
import com.boyouquan.model.Blog;
import com.boyouquan.model.NewBlogInfo;
import com.boyouquan.service.BlogService;
import com.boyouquan.util.NewPagination;
import com.boyouquan.util.NewPaginationBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDaoMapper blogDaoMapper;

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
        blogInfo.setPostsCount(10L);
        blogInfo.setAccessCount(10L);
        blogInfo.setLatestUpdatedAt(new Date());

        return blogInfo;
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
