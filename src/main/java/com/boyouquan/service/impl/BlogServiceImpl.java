package com.boyouquan.service.impl;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.dao.BlogDaoMapper;
import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogInfo;
import com.boyouquan.model.Post;
import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
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
    @Autowired
    private AccessService accessService;

    @Override
    public String getBlogAdminSmallImageURLByDomainName(String blogDomainName) {
        Blog blog = getByDomainName(blogDomainName);
        if (null == blog) {
            return "";
        }

        return String.format(CommonConstants.GRAVATAR_ADDRESS_SMALL_SIZE, CommonUtils.md5(blog.getAdminEmail()));
    }

    @Override
    public String getBlogAdminLargeImageURLByDomainName(String blogDomainName) {
        Blog blog = getByDomainName(blogDomainName);
        if (null == blog) {
            return "";
        }

        return String.format(CommonConstants.GRAVATAR_ADDRESS_LARGE_SIZE, CommonUtils.md5(blog.getAdminEmail()));
    }

    @Override
    public Blog getByRandom() {
        return blogDaoMapper.getByRandom();
    }

    @Override
    public Long countAll() {
        return blogDaoMapper.countAll();
    }

    @Override
    public List<Blog> listAll() {
        return blogDaoMapper.listAll();
    }

    @Override
    public List<Blog> listRecentCollected(int limit) {
        return blogDaoMapper.listRecentCollected(limit);
    }

    @Override
    public BlogInfo getBlogInfoByDomainName(String domainName) {
        Blog blog = getByDomainName(domainName);
        if (null == blog) {
            return null;
        }

        // assemble
        return assembleBlogInfo(blog, CommonConstants.ALL_POST_COUNT_LIMIT);
    }

    @Override
    public Pagination<BlogInfo> listBlogInfosWithKeyWord(String keyword, int page, int size) {
        if (page < 1 || size <= 0) {
            return PaginationBuilder.buildEmptyResults();
        }

        // list
        List<BlogInfo> blogInfos = new ArrayList<>();
        Pagination<Blog> blogPagination = listWithKeyWord(keyword, page, size);
        for (Blog blog : blogPagination.getResults()) {
            // assemble
            BlogInfo blogInfo = assembleBlogInfo(blog, CommonConstants.LATEST_POST_COUNT_LIMIT);
            blogInfos.add(blogInfo);
        }
        long total = blogPagination.getTotal();
        return PaginationBuilder.<BlogInfo>newBuilder()
                .pageNo(page)
                .pageSize(size)
                .total(total)
                .results(blogInfos).build();
    }

    @Override
    public Pagination<Blog> listWithKeyWord(String keyword, int page, int size) {
        if (page < 1 || size <= 0) {
            return PaginationBuilder.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<Blog> blogs = blogDaoMapper.listWithKeyWord(keyword, offset, size);
        Long total = blogDaoMapper.countWithKeyword(keyword);
        return PaginationBuilder.<Blog>newBuilder()
                .pageNo(page)
                .pageSize(size)
                .total(total)
                .results(blogs).build();
    }

    @Override
    public boolean existsByRssAddress(String rssAddress) {
        return blogDaoMapper.existsByRssAddress(rssAddress);
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
    public Blog getByAddress(String address) {
        return blogDaoMapper.getByAddress(address);
    }

    @Override
    public Blog getByRSSAddress(String rssAddress) {
        return blogDaoMapper.getByRSSAddress(rssAddress);
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

    private BlogInfo assembleBlogInfo(Blog blog, int postCountLimit) {
        BlogInfo blogInfo = new BlogInfo();
        BeanUtils.copyProperties(blog, blogInfo);

        String blogDomainName = blog.getDomainName();
        Long count = postService.countByBlogDomainName(blogDomainName);
        blogInfo.setPostCount(count);
        Date latestUpdatedAt = postService.getLatestPublishedAtByBlogDomainName(blogDomainName);
        Long accessCount = accessService.countByBlogDomainName(blogDomainName);
        blogInfo.setAccessCount(accessCount);
        blogInfo.setLatestPublishedAt(latestUpdatedAt);

        String blogAdminSmallImageURL = getBlogAdminSmallImageURLByDomainName(blogDomainName);
        blogInfo.setBlogAdminSmallImageURL(blogAdminSmallImageURL);

        String blogAdminLargeImageURL = getBlogAdminLargeImageURLByDomainName(blogDomainName);
        blogInfo.setBlogAdminLargeImageURL(blogAdminLargeImageURL);

        List<Post> latestPosts = postService.listByBlogDomainName(blog.getDomainName(), postCountLimit);
        blogInfo.setPosts(latestPosts);

        return blogInfo;
    }

}
