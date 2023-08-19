package com.boyouquan.service;

import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogStatus;

public interface BlogStatusService {

    void detectBlogStatus(Blog blog);

    boolean isStatusOkByBlogDomainName(String blogDomainName);

    BlogStatus getLatestByBlogDomainName(String blogDomainName);

    void save(String blogDomainName, BlogStatus.Status status, int code, String reason);

}
