package com.boyouquan.dao;

import com.boyouquan.model.BlogLocation;

public interface BlogLocationDaoMapper {

    boolean existsByDomainName(String domainName);

    BlogLocation getByDomainName(String domainName);

    void save(BlogLocation blogLocation);

    void update(BlogLocation blogLocation);

}
