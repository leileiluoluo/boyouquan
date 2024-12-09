package com.boyouquan.service.impl;

import com.boyouquan.model.*;
import com.boyouquan.service.*;
import com.boyouquan.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AnnualReportServiceImpl implements AnnualReportService {

    private final Logger logger = LoggerFactory.getLogger(AnnualReportServiceImpl.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private PinHistoryService pinHistoryService;

    @Autowired
    private EmailService emailService;

    @Override
    public String getAnnualReport(String domainName) {
        Blog blog = blogService.getByDomainName(domainName);
        if (null == blog) {
            return null;
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        BlogAnnualReport report = new BlogAnnualReport();
        report.setYear(currentYear);
        report.setBlogDomainName(domainName);
        report.setBlogName(blog.getName());
        report.setBlogCollectedAt(CommonUtils.dateCommonFormatDisplay(blog.getCollectedAt()));

        // joinDaysTillNow
        long joinDaysTillNow = ChronoUnit.DAYS.between(blog.getCollectedAt().toInstant(), Instant.now());
        report.setJoinDaysTillNow(joinDaysTillNow);

        Date currentYearFirstDay = getCurrentYearFirstDay();

        // joinedAfterStartDay
        boolean joinedAfterStartDay = blog.getCollectedAt().after(currentYearFirstDay);
        report.setJoinedAfterYearStartDay(joinedAfterStartDay);

        // startDate
        Date startDate = joinedAfterStartDay ? blog.getCollectedAt() : currentYearFirstDay;

        // postCountTillNow
        long postCountTillNow = postService.countByBlogDomainName(domainName, startDate);
        report.setPostCountTillNow(postCountTillNow);

        // accessCountTillNow
        long accessCountTillNow = accessService.countByBlogDomainName(domainName, startDate);
        report.setAccessCountTillNow(accessCountTillNow);

        // maxMonthPublish
        MonthPublish maxMonthPublish = getMaxMonthPublish(domainName, startDate);
        report.setMaxMonthPublish(maxMonthPublish);

        // maxMonthAccess
        MonthAccess maxMonthAccess = getMaxMonthAccess(domainName, startDate);
        report.setMaxMonthAccess(maxMonthAccess);

        // mostAccessedPost
        BlogLinkAccess blogLinkAccess = accessService.getMostAccessedLinkByDomainName(domainName, blog.getAddress(), startDate);
        if (null != blogLinkAccess) {
            Post post = postService.getByLink(blogLinkAccess.getLink());
            if (null != post) {
                report.setMostAccessedPost(post);
                report.setMostAccessedPostAccessCount(blogLinkAccess.getAccessCount());
            }
        }

        // latestUpdatedAt
        Date latestUpdatedAt = postService.getLatestPublishedAtByBlogDomainName(domainName);
        if (null != latestUpdatedAt) {
            report.setLatestUpdatedAt(CommonUtils.dateHourSecondCommonFormatDisplay(latestUpdatedAt));
        }

        // recommendedPosts
        List<Post> recommendedPosts = postService.listRecommendedByBlogDomainName(domainName, startDate);
        report.setRecommendedPosts(recommendedPosts);

        // pinnedPosts
        List<Post> pinnedPosts = getPinnedPosts(domainName, startDate);
        report.setPinnedPosts(pinnedPosts);

        return emailService.getBlogAnnualReport(report);
    }

    private Date getCurrentYearFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    private MonthPublish getMaxMonthPublish(String domainName, Date startDate) {
        List<MonthPublish> monthPublishes = postService.getBlogPostPublishSeriesInLatestOneYear(domainName);
        if (null != monthPublishes && !monthPublishes.isEmpty()) {
            MonthPublish monthPublish = monthPublishes.stream().max(Comparator.comparingInt(MonthPublish::getCount)).get();
            if (monthPublish.getCount() > 0) {
                try {
                    Date monthDate = new SimpleDateFormat("yyyy/MM").parse(monthPublish.getMonth());
                    return monthDate.after(startDate) ? monthPublish : null;
                } catch (ParseException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    private MonthAccess getMaxMonthAccess(String domainName, Date startDate) {
        List<MonthAccess> monthAccesses = accessService.getBlogAccessSeriesInLatestOneYear(domainName);
        if (null != monthAccesses && !monthAccesses.isEmpty()) {
            MonthAccess monthAccess = monthAccesses.stream().max(Comparator.comparingInt(MonthAccess::getCount)).get();
            if (monthAccess.getCount() > 0) {
                try {
                    Date monthDate = new SimpleDateFormat("yyyy/MM").parse(monthAccess.getMonth());
                    return monthDate.after(startDate) ? monthAccess : null;
                } catch (ParseException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    private List<Post> getPinnedPosts(String domainName, Date startDate) {
        List<String> pinnedLinks = pinHistoryService.listLinksByBlogDomainName(domainName, startDate);
        List<Post> pinnedPosts = new ArrayList<>();
        if (null != pinnedLinks) {
            for (String link : pinnedLinks) {
                Post post = postService.getByLink(link);
                if (null != post) {
                    pinnedPosts.add(post);
                }
            }
        }
        return pinnedPosts;
    }

}
