package com.boyouquan.service.impl;

import com.boyouquan.dao.PostDaoMapper;
import com.boyouquan.model.BlogDomainNamePublish;
import com.boyouquan.model.Post;
import com.boyouquan.service.PostService;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDaoMapper postDaoMapper;

    @Override
    public BlogDomainNamePublish getMostPublishedInLatestOneMonth() {
        return postDaoMapper.getMostPublishedInLatestOneMonth();
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
    public List<Post> listByBlogDomainName(String blogDomainName, int limit) {
        return postDaoMapper.listByBlogDomainName(blogDomainName, limit);
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
    public void batchSave(List<Post> posts) {
        if (!posts.isEmpty()) {
            postDaoMapper.batchSave(posts);
        }
    }

}
