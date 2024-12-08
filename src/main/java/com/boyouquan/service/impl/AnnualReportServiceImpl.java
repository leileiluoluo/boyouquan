package com.boyouquan.service.impl;

import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogAnnualReport;
import com.boyouquan.model.Post;
import com.boyouquan.service.*;
import com.boyouquan.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AnnualReportServiceImpl implements AnnualReportService {

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

        // recommendedPosts
        List<Post> recommendedPosts = postService.listRecommendedByBlogDomainName(domainName, startDate);
        report.setRecommendedPosts(recommendedPosts.stream().limit(5).toList());

        // pinnedPosts
        List<Post> pinnedPosts = getPinnedPosts(domainName, startDate);
        report.setPinnedPosts(pinnedPosts.stream().limit(5).toList());

        return emailService.getBlogAnnualReport(report);
    }

    private Date getCurrentYearFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
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
