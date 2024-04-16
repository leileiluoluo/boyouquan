package com.boyouquan.service;

import com.boyouquan.model.BlogLocation;

public interface BlogLocationService {

    boolean existsByDomainName(String domainName);

    BlogLocation getByDomainName(String domainName);

    void save(BlogLocation blogLocation);

    void update(BlogLocation blogLocation);

    void deleteByDomainName(String domainName);

}
