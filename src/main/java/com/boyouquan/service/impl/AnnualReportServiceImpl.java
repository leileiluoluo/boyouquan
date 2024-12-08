package com.boyouquan.service.impl;

import com.boyouquan.model.Blog;
import com.boyouquan.model.BlogAnnualReport;
import com.boyouquan.service.*;
import com.boyouquan.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

@Service
public class AnnualReportServiceImpl implements AnnualReportService {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;

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

        // postCountTillNow
        long postCountTillNow = postService.countByBlogDomainName(domainName, currentYearFirstDay);
        report.setPostCountTillNow(postCountTillNow);

        // accessCountTillNow
        long accessCountTillNow = accessService.countByBlogDomainName(domainName, currentYearFirstDay);
        report.setAccessCountTillNow(accessCountTillNow);

        return emailService.getBlogAnnualReport(report);
    }

    private Date getCurrentYearFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

}
