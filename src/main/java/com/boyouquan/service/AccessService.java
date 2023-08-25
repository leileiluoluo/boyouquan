package com.boyouquan.service;

import com.boyouquan.model.Access;
import com.boyouquan.model.BlogDomainNameAccess;
import com.boyouquan.model.MonthAccess;

import java.util.List;

public interface AccessService {

    Long countAll();

    BlogDomainNameAccess getMostAccessedBlogDomainNameInLatestOneMonth();

    List<MonthAccess> getBlogAccessSeriesInLatestOneYear(String blogDomainName);

    Long countByBlogDomainName(String blogDomainName);

    Long countByLink(String link);

    void save(Access access);

}
