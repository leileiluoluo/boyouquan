package com.boyouquan.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class BlogAnnualReport {

    private String blogDomainName;
    private int year;
    private String blogName;
    private String blogCollectedAt;
    private long joinDaysTillNow;

    private boolean joinedAfterYearStartDay;
    private long postCountTillNow;
    private String postCountExceedPercent;
    private long accessCountTillNow;

    private MonthPublish maxMonthPublish;
    private MonthAccess maxMonthAccess;

    private Post mostAccessedPost;
    private long mostAccessedPostAccessCount;

    private String latestUpdatedAt;
    private Post mostLatePublishedPost;
    private String mostLatePublishedPostPublishedAt;

    private List<Post> recommendedPosts = Collections.emptyList();
    private List<Post> pinnedPosts = Collections.emptyList();

    private long planetShuttleInitiatedCount;

    private boolean isSponsor;

}
