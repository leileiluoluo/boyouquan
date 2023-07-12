package com.boyouquan.service;

import com.boyouquan.model.BlogAccess;
import com.boyouquan.model.BlogAccessSummary;

public interface BlogAccessService {

    BlogAccessSummary getMostAccessedBlogByLatestMonth();

    Long countBlogAccessByLinkPrefix(String linkPrefix);

    Long countBlogAccessByLink(String link);

    void saveBlogAccess(BlogAccess blogAccess);

    Long totalCount();

}
