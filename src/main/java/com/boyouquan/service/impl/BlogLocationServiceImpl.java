package com.boyouquan.service.impl;

import com.boyouquan.dao.BlogLocationDaoMapper;
import com.boyouquan.helper.IPInfoHelper;
import com.boyouquan.model.BlogLocation;
import com.boyouquan.service.BlogLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogLocationServiceImpl implements BlogLocationService {

    @Autowired
    private BlogLocationDaoMapper blogLocationDaoMapper;
    @Autowired
    private IPInfoHelper ipInfoHelper;

    @Override
    public boolean existsByDomainName(String domainName) {
        return blogLocationDaoMapper.existsByDomainName(domainName);
    }

    @Override
    public BlogLocation getByDomainName(String domainName) {
        return blogLocationDaoMapper.getByDomainName(domainName);
    }

    @Override
    public void refreshLocation(String domainName) {
        BlogLocation blogLocation = ipInfoHelper.getIpInfoByDomainName(domainName);

        if (null != blogLocation) {
            BlogLocation blogLocationStored = getByDomainName(domainName);
            if (null != blogLocationStored) {
                if (!blogLocation.getLocation().equals(blogLocationStored.getLocation())) {
                    blogLocationDaoMapper.update(blogLocation);
                }
            } else {
                blogLocationDaoMapper.save(blogLocation);
            }
        }
    }

    @Override
    public void deleteByDomainName(String domainName) {
        blogLocationDaoMapper.deleteByDomainName(domainName);
    }

}
