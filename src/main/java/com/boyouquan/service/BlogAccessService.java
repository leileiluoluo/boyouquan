package com.boyouquan.service;

import com.boyouquan.model.BlogAccess;
import com.boyouquan.model.BlogAccessSummary;
import com.boyouquan.model.DayAccess;

import java.util.List;

public interface BlogAccessService {

    List<DayAccess> getBlogAccessSeriesInLatestOneMonth(String blogAddress);

    BlogAccessSummary getMostAccessedBlogByLatestMonth();

    Long countBlogAccessByLinkPrefix(String linkPrefix);

    Long countBlogAccessByLink(String link);

    void saveBlogAccess(BlogAccess blogAccess);

    Long totalCount();

}
