package com.boyouquan.service;

import com.boyouquan.model.BlogLocation;

public interface BlogLocationService {

    boolean existsByDomainName(String domainName);

    BlogLocation getByDomainName(String domainName);

    void refreshLocation(String domainName);

    void deleteByDomainName(String domainName);

}
