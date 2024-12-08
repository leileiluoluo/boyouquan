package com.boyouquan.model;

import lombok.Data;

import java.util.List;

@Data
public class BlogAnnualReport {

    private String blogDomainName;
    private int year;
    private String blogName;
    private String blogCollectedAt;
    private long joinDaysTillNow;
    private long postCountTillNow;
    private long accessCountTillNow;
    private List<Post> recommendedPosts;
    private List<Post> pinnedPosts;

}
