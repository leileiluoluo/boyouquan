package com.boyouquan.service.impl;

import com.boyouquan.dao.BlogLocationDaoMapper;
import com.boyouquan.model.BlogLocation;
import com.boyouquan.service.BlogLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogLocationServiceImpl implements BlogLocationService {

    @Autowired
    private BlogLocationDaoMapper blogLocationDaoMapper;

    @Override
    public boolean existsByDomainName(String domainName) {
        return blogLocationDaoMapper.existsByDomainName(domainName);
    }

    @Override
    public BlogLocation getByDomainName(String domainName) {
        return blogLocationDaoMapper.getByDomainName(domainName);
    }

    @Override
    public void save(BlogLocation blogLocation) {
        blogLocationDaoMapper.save(blogLocation);
    }

    @Override
    public void update(BlogLocation blogLocation) {
        blogLocationDaoMapper.update(blogLocation);
    }

}
