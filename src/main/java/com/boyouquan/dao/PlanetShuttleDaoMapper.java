package com.boyouquan.dao;

import com.boyouquan.model.BlogDomainNameInitiated;
import com.boyouquan.model.MonthInitiated;
import com.boyouquan.model.PlanetShuttle;

import java.util.List;

public interface PlanetShuttleDaoMapper {

    BlogDomainNameInitiated getMostInitiatedBlogDomainNameInLastMonth();

    String getLatestInitiatedYearMonthStr(String blogDomainName);

    List<MonthInitiated> getBlogInitiatedSeriesInLatestOneYear(String blogDomainName);

    Long countInitiatedByBlogDomainName(String blogDomainName);

    void save(PlanetShuttle planetShuttle);

}
