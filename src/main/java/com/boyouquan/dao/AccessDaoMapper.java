package com.boyouquan.dao;

import com.boyouquan.model.Access;
import com.boyouquan.model.BlogDomainNameAccess;
import com.boyouquan.model.DayAccess;

import java.util.List;

public interface AccessDaoMapper {

    Long countAll();

    BlogDomainNameAccess getMostAccessedBlogDomainNameInLatestOneMonth();

    List<DayAccess> getBlogAccessSeriesInLatestOneMonth(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName);

    Long countByLink(String link);

    void save(Access access);

}
