package com.boyouquan.dao;

import com.boyouquan.model.BlogAccess;
import com.boyouquan.model.BlogAccessSummary;
import com.boyouquan.model.DayAccess;

import java.util.List;

public interface BlogAccessDaoMapper {

    List<DayAccess> getBlogAccessSeriesInLatestOneMonth(String blogAddress);

    BlogAccessSummary getMostAccessedBlogByLatestMonth();

    Long countBlogAccessByLinkPrefix(String linkPrefix);

    Long countByLink(String link);

    void save(BlogAccess blogAccess);

    Long countTotal();

}
