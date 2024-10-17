package com.boyouquan.controller;

import com.boyouquan.model.BlogInfo;
import com.boyouquan.model.MonthAccess;
import com.boyouquan.model.MonthInitiated;
import com.boyouquan.model.MonthPublish;
import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PlanetShuttleService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blogs")
public class BlogDetailRestController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private PlanetShuttleService planetShuttleService;

    @GetMapping("/{domainName}/**")
    public Map<String, Object> getBlogByDomainName(@PathVariable("domainName") String domainName, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        // parse domain from request URL
        String requestURL = request.getRequestURL().toString();
        String blogDomainName = requestURL.split("/blogs/")[1];
        if (!blogDomainName.contains("/")) {
            blogDomainName = domainName;
        }

        // get blog info
        BlogInfo blogInfo = blogService.getBlogInfoByDomainName(blogDomainName);
        if (null == blogInfo) {
            result.put("error", "blog not exists");
            return result;
        }

        result.put("blogInfo", blogInfo);

        // planet shuttle charts
        boolean showLatestInitiatedChart = false;
        String latestInitiatedYearMonthStr = planetShuttleService.getLatestInitiatedYearMonthStr(blogInfo.getDomainName());
        if (StringUtils.isNotBlank(latestInitiatedYearMonthStr)) {
            Date latestInitiated = CommonUtils.yearMonthStr2Date(latestInitiatedYearMonthStr);
            boolean oneYearAgo = CommonUtils.isDateOneYearAgo(latestInitiated);

            if (!oneYearAgo) {
                showLatestInitiatedChart = true;
                List<MonthInitiated> monthInitiatedList = planetShuttleService.getBlogInitiatedSeriesInLatestOneYear(blogInfo.getDomainName());
                String[] yearlyInitiatedDataLabels = monthInitiatedList.stream().map(MonthInitiated::getMonth).toArray(String[]::new);
                Integer[] yearlyInitiatedDataValues = monthInitiatedList.stream().map(MonthInitiated::getCount).toArray(Integer[]::new);
                result.put("yearlyInitiatedDataLabels", yearlyInitiatedDataLabels);
                result.put("yearlyInitiatedDataValues", yearlyInitiatedDataValues);
            }
        }
        result.put("showLatestInitiatedChart", showLatestInitiatedChart);

        // monthly access charts
        List<MonthAccess> monthAccessList = accessService.getBlogAccessSeriesInLatestOneYear(blogInfo.getDomainName());
        String[] yearlyAccessDataLabels = monthAccessList.stream().map(MonthAccess::getMonth).toArray(String[]::new);
        Integer[] yearlyAccessDataValues = monthAccessList.stream().map(MonthAccess::getCount).toArray(Integer[]::new);

        result.put("yearlyAccessDataLabels", yearlyAccessDataLabels);
        result.put("yearlyAccessDataValues", yearlyAccessDataValues);

        // yearly publish charts
        boolean showLatestPublishedAtChart = false;
        Date latestPublishAt = postService.getLatestPublishedAtByBlogDomainName(blogInfo.getDomainName());
        showLatestPublishedAtChart = !CommonUtils.isDateOneYearAgo(latestPublishAt);
        result.put("showLatestPublishedAtChart", showLatestPublishedAtChart);
        if (showLatestPublishedAtChart) {
            List<MonthPublish> monthPublishList = postService.getBlogPostPublishSeriesInLatestOneYear(blogInfo.getDomainName());
            String[] yearlyPublishDataLabels = monthPublishList.stream().map(MonthPublish::getMonth).toArray(String[]::new);
            Integer[] yearlyPublishDataValues = monthPublishList.stream().map(MonthPublish::getCount).toArray(Integer[]::new);

            result.put("yearlyPublishDataLabels", yearlyPublishDataLabels);
            result.put("yearlyPublishDataValues", yearlyPublishDataValues);
        }

        // random blogs
        List<BlogInfo> randomBlogInfos = blogService.listByRandom(List.of(blogInfo.getDomainName()), 2)
                .stream()
                .map(blog -> blogService.getBlogInfoByDomainName(blog.getDomainName()))
                .toList();

        result.put("randomBlogInfos", randomBlogInfos);

        return result;
    }

}
