package com.boyouquan.service.impl;

import com.boyouquan.dao.PostDaoMapper;
import com.boyouquan.model.Post;
import com.boyouquan.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostDaoMapper postDaoMapper;

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
    public void batchSave(List<Post> posts) {
        postDaoMapper.batchSave(posts);
    }

}
