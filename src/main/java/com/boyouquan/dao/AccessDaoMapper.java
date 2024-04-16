package com.boyouquan.dao;

import com.boyouquan.model.Access;
import com.boyouquan.model.BlogDomainNameAccess;
import com.boyouquan.model.MonthAccess;

import java.util.List;

public interface AccessDaoMapper {

    Long countAll();

    BlogDomainNameAccess getMostAccessedBlogDomainNameInLastMonth();

    List<MonthAccess> getBlogAccessSeriesInLatestOneYear(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName);

    Long countByLink(String link);

    void save(Access access);

    void deleteByBlogDomainName(String blogDomainName);

}
