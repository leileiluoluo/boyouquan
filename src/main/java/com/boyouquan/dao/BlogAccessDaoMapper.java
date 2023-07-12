package com.boyouquan.dao;

import com.boyouquan.model.BlogAccess;
import com.boyouquan.model.BlogAccessSummary;

public interface BlogAccessDaoMapper {

    BlogAccessSummary getMostAccessedBlogByLatestMonth();

    Long countBlogAccessByLinkPrefix(String linkPrefix);

    Long countByLink(String link);

    void save(BlogAccess blogAccess);

    Long countTotal();

}
