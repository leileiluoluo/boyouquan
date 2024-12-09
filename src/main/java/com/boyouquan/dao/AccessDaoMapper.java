package com.boyouquan.dao;

import com.boyouquan.model.Access;
import com.boyouquan.model.BlogDomainNameAccess;
import com.boyouquan.model.BlogLinkAccess;
import com.boyouquan.model.MonthAccess;

import java.util.Date;
import java.util.List;

public interface AccessDaoMapper {

    Long countAll();

    BlogDomainNameAccess getMostAccessedBlogDomainNameInLastMonth();

    BlogLinkAccess getMostAccessedLinkByBlogDomainName(String blogDomainName, String blogAddress, Date startDate);

    List<MonthAccess> getBlogAccessSeriesInLatestOneYear(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName);

    Long countByBlogDomainNameAndStartDate(String blogDomainName, Date startDate);

    Long countByLink(String link);

    void save(Access access);

    void deleteByBlogDomainName(String blogDomainName);

}
