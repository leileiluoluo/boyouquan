package com.boyouquan.service;

import com.boyouquan.model.Access;
import com.boyouquan.model.BlogDomainNameAccess;
import com.boyouquan.model.MonthAccess;

import java.util.Date;
import java.util.List;

public interface AccessService {

    Long countAll();

    BlogDomainNameAccess getMostAccessedBlogDomainNameInLastMonth();

    List<MonthAccess> getBlogAccessSeriesInLatestOneYear(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName, Date startDate);

    Long countByLink(String link);

    void save(Access access);

    void deleteByBlogDomainName(String blogDomainName);

}
