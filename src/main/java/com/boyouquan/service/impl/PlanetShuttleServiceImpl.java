package com.boyouquan.service.impl;

import com.boyouquan.dao.PlanetShuttleDaoMapper;
import com.boyouquan.helper.IPControlHelper;
import com.boyouquan.model.BlogDomainNameInitiated;
import com.boyouquan.model.MonthInitiated;
import com.boyouquan.model.PlanetShuttle;
import com.boyouquan.service.PlanetShuttleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanetShuttleServiceImpl implements PlanetShuttleService {

    @Autowired
    private PlanetShuttleDaoMapper planetShuttleDaoMapper;
    @Autowired
    private IPControlHelper ipControlHelper;

    @Override
    public BlogDomainNameInitiated getMostInitiatedBlogDomainNameInLastMonth() {
        return planetShuttleDaoMapper.getMostInitiatedBlogDomainNameInLastMonth();
    }

    @Override
    public String getLatestInitiatedYearMonthStr(String blogDomainName) {
        return planetShuttleDaoMapper.getLatestInitiatedYearMonthStr(blogDomainName);
    }

    @Override
    public List<MonthInitiated> getBlogInitiatedSeriesInLatestOneYear(String blogDomainName) {
        return planetShuttleDaoMapper.getBlogInitiatedSeriesInLatestOneYear(blogDomainName);
    }

    @Override
    public Long countInitiatedByBlogDomainName(String blogDomainName) {
        return planetShuttleDaoMapper.countInitiatedByBlogDomainName(blogDomainName);
    }

    @Override
    public void save(PlanetShuttle planetShuttle) {
        String ip = planetShuttle.getIp();
        String link = "http://" + planetShuttle.getBlogDomainName() + "?type=planet_shuttle";
        if (!ipControlHelper.alreadyAccessed(ip, link)) {
            planetShuttleDaoMapper.save(planetShuttle);

            ipControlHelper.access(ip, link);
        }
    }

}
