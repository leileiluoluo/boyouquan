package com.boyouquan.service.impl;

import com.boyouquan.dao.PostDaoMapper;
import com.boyouquan.model.BlogDomainNamePublish;
import com.boyouquan.model.MonthPublish;
import com.boyouquan.model.Post;
import com.boyouquan.service.PostService;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private PostDaoMapper postDaoMapper;

    @Override
    public BlogDomainNamePublish getMostPublishedInLatestOneMonth() {
        return postDaoMapper.getMostPublishedInLatestOneMonth();
    }

    @Override
    public List<MonthPublish> getBlogPostPublishSeriesInLatestOneYear(String blogDomainName) {
        return postDaoMapper.getBlogPostPublishSeriesInLatestOneYear(blogDomainName);
    }

    @Override
    public Pagination<Post> listWithKeyWord(String keyword, int page, int size) {
        if (page < 1 || size <= 0) {
            return PaginationBuilder.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<Post> posts = postDaoMapper.listWithKeyWord(keyword, offset, size);
        Long total = postDaoMapper.countWithKeyWord(keyword);
        return PaginationBuilder.<Post>newBuilder()
                .pageNo(page)
                .pageSize(size)
                .total(total)
                .results(posts).build();
    }

    @Override
    public Long countAll() {
        return postDaoMapper.countAll();
    }

    @Override
    public Date getLatestPublishedAtByBlogDomainName(String blogDomainName) {
        return postDaoMapper.getLatestPublishedAtByBlogDomainName(blogDomainName);
    }

    @Override
    public Long countByBlogDomainName(String blogDomainName) {
        return postDaoMapper.countByBlogDomainName(blogDomainName);
    }

    @Override
    public Pagination<Post> listByDraftAndBlogDomainName(boolean draft, String blogDomainName, int page, int size) {
        if (page < 1 || size <= 0) {
            return PaginationBuilder.buildEmptyResults();
        }

        int offset = (page - 1) * size;
        List<Post> posts = postDaoMapper.listByDraftAndBlogDomainName(draft, blogDomainName, offset, size);
        Long total = postDaoMapper.countByDraftAndBlogDomainName(draft, blogDomainName);
        return PaginationBuilder.<Post>newBuilder()
                .pageNo(page)
                .pageSize(size)
                .total(total)
                .results(posts).build();
    }

    @Override
    public boolean existsByLink(String link) {
        return postDaoMapper.existsByLink(link);
    }

    @Override
    public Post getByLink(String link) {
        return postDaoMapper.getByLink(link);
    }

    @Override
    public boolean batchSave(List<Post> posts) {
        try {
            if (null != posts && !posts.isEmpty()) {
                postDaoMapper.batchSave(posts);
                return true;
            }
        } catch (Exception e) {
            logger.error("batch save failed!", e);
        }
        return false;
    }

    @Override
    public boolean batchUpdateDraftByBlogDomainName(String blogDomainName, boolean draft) {
        try {
            postDaoMapper.batchUpdateDraftByBlogDomainName(blogDomainName, draft);
            return true;
        } catch (Exception e) {
            logger.error("batch save failed!", e);
        }
        return false;
    }

    @Override
    public void deleteByBlogDomainName(String blogDomainName) {
        postDaoMapper.deleteByBlogDomainName(blogDomainName);
    }

}
