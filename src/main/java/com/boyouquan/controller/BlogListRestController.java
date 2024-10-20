package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.*;
import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PlanetShuttleService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blogs")
public class BlogListRestController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private PlanetShuttleService planetShuttleService;

    @GetMapping("")
    public ResponseEntity<?> list(
            @RequestParam(value = "domainName", required = false) String domainName,
            @RequestParam(value = "sort", required = false, defaultValue = "collect_time") String sort,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        // single blog
        if (StringUtils.isNotBlank(domainName)) {
            BlogInfo blogInfo = blogService.getBlogInfoByDomainName(domainName);
            return ResponseEntity.ok(blogInfo);
        }

        // blog list
        if (null == keyword) {
            keyword = "";
        }
        keyword = StringUtils.trim(keyword);

        Pagination<BlogInfo> blogs = blogService.listBlogInfosWithKeyWord(BlogSortType.of(sort), keyword, page, CommonConstants.DEFAULT_PAGE_SIZE);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/random-blogs")
    public List<BlogInfo> getRandomBlogsUnderBlog(@RequestParam("domainName") String domainName) {
        return blogService.listByRandom(List.of(domainName), 2)
                .stream()
                .map(blog -> blogService.getBlogInfoByDomainName(blog.getDomainName()))
                .toList();
    }

    @GetMapping("/posts")
    public List<Post> getPostsUnderBlog(@RequestParam("domainName") String domainName) {
        return postService.listByDraftAndBlogDomainName(false, domainName, 1, CommonConstants.ALL_POST_COUNT_LIMIT)
                .getResults();
    }

    @GetMapping("/charts")
    public Map<String, Object> getBlogCharts(@RequestParam("domainName") String domainName) {
        Map<String, Object> result = new HashMap<>();

        // planet shuttle charts
        boolean showLatestInitiatedChart = false;
        String latestInitiatedYearMonthStr = planetShuttleService.getLatestInitiatedYearMonthStr(domainName);
        if (StringUtils.isNotBlank(latestInitiatedYearMonthStr)) {
            Date latestInitiated = CommonUtils.yearMonthStr2Date(latestInitiatedYearMonthStr);
            boolean oneYearAgo = CommonUtils.isDateOneYearAgo(latestInitiated);

            if (!oneYearAgo) {
                showLatestInitiatedChart = true;
                List<MonthInitiated> monthInitiatedList = planetShuttleService.getBlogInitiatedSeriesInLatestOneYear(domainName);
                String[] yearlyInitiatedDataLabels = monthInitiatedList.stream().map(MonthInitiated::getMonth).toArray(String[]::new);
                Integer[] yearlyInitiatedDataValues = monthInitiatedList.stream().map(MonthInitiated::getCount).toArray(Integer[]::new);
                result.put("yearlyInitiatedDataLabels", yearlyInitiatedDataLabels);
                result.put("yearlyInitiatedDataValues", yearlyInitiatedDataValues);
            }
        }
        result.put("showLatestInitiatedChart", showLatestInitiatedChart);

        // monthly access charts
        List<MonthAccess> monthAccessList = accessService.getBlogAccessSeriesInLatestOneYear(domainName);
        String[] yearlyAccessDataLabels = monthAccessList.stream().map(MonthAccess::getMonth).toArray(String[]::new);
        Integer[] yearlyAccessDataValues = monthAccessList.stream().map(MonthAccess::getCount).toArray(Integer[]::new);

        result.put("yearlyAccessDataLabels", yearlyAccessDataLabels);
        result.put("yearlyAccessDataValues", yearlyAccessDataValues);

        // yearly publish charts
        boolean showLatestPublishedAtChart = false;
        Date latestPublishAt = postService.getLatestPublishedAtByBlogDomainName(domainName);
        showLatestPublishedAtChart = !CommonUtils.isDateOneYearAgo(latestPublishAt);
        result.put("showLatestPublishedAtChart", showLatestPublishedAtChart);
        if (showLatestPublishedAtChart) {
            List<MonthPublish> monthPublishList = postService.getBlogPostPublishSeriesInLatestOneYear(domainName);
            String[] yearlyPublishDataLabels = monthPublishList.stream().map(MonthPublish::getMonth).toArray(String[]::new);
            Integer[] yearlyPublishDataValues = monthPublishList.stream().map(MonthPublish::getCount).toArray(Integer[]::new);

            result.put("yearlyPublishDataLabels", yearlyPublishDataLabels);
            result.put("yearlyPublishDataValues", yearlyPublishDataValues);
        }

        return result;
    }

}
