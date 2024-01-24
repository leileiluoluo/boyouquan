package com.boyouquan.service.impl;

import com.boyouquan.dao.AccessDaoMapper;
import com.boyouquan.helper.IPControlHelper;
import com.boyouquan.model.Access;
import com.boyouquan.model.BlogDomainNameAccess;
import com.boyouquan.model.MonthAccess;
import com.boyouquan.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessServiceImpl implements AccessService {

    @Autowired
    private AccessDaoMapper accessDaoMapper;
    @Autowired
    private IPControlHelper ipControlHelper;

    @Override
    public Long countAll() {
        return accessDaoMapper.countAll();
    }

    @Override
    public BlogDomainNameAccess getMostAccessedBlogDomainNameInLastMonth() {
        return accessDaoMapper.getMostAccessedBlogDomainNameInLastMonth();
    }

    @Override
    public List<MonthAccess> getBlogAccessSeriesInLatestOneYear(String blogDomainName) {
        return accessDaoMapper.getBlogAccessSeriesInLatestOneYear(blogDomainName);
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
        String ip = access.getIp();
        String link = access.getLink();

        if (!ipControlHelper.alreadyAccessed(ip, link)) {
            // save
            accessDaoMapper.save(access);
            ipControlHelper.access(ip, link);
        }
    }

}
