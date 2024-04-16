package com.boyouquan.dao;

import com.boyouquan.model.BlogStatus;

public interface BlogStatusDaoMapper {

    BlogStatus getLatestByBlogDomainName(String blogDomainName);

    void save(BlogStatus blogStatus);

    void deleteByBlogDomainName(String blogDomainName);

}
