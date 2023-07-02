package com.boyouquan.service;

import com.boyouquan.model.BlogAccess;

public interface BlogAccessService {

    Long countBlogAccessByLink(String link);

    void saveBlogAccess(BlogAccess blogAccess);

    Long totalCount();

}
