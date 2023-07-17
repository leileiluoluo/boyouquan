package com.boyouquan.service.impl;

import com.boyouquan.dao.AccessDaoMapper;
import com.boyouquan.model.Access;
import com.boyouquan.model.BlogDomainNameAccess;
import com.boyouquan.model.DayAccess;
import com.boyouquan.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessServiceImpl implements AccessService {

    @Autowired
    private AccessDaoMapper accessDaoMapper;

    @Override
    public Long countAll() {
        return accessDaoMapper.countAll();
    }

    @Override
    public BlogDomainNameAccess getMostAccessedBlogDomainNameInLatestOneMonth() {
        return accessDaoMapper.getMostAccessedBlogDomainNameInLatestOneMonth();
    }

    @Override
    public List<DayAccess> getBlogAccessSeriesInLatestOneMonth(String blogDomainName) {
        return accessDaoMapper.getBlogAccessSeriesInLatestOneMonth(blogDomainName);
    }

    @Override
    public Long countByBlogDomainName(String blogDomainName) {
        return accessDaoMapper.countByBlogDomainName(blogDomainName);
    }

    @Override
    public Long countByLink(String link) {
        return accessDaoMapper.countByLink(link);
    }

    @Override
    public void save(Access access) {
        accessDaoMapper.save(access);
    }

}
