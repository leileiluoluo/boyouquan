package com.boyouquan.model;

import lombok.Data;

@Data
public class BlogAnnualReport {

    private String blogDomainName;
    private String year;
    private String blogName;
    private String blogCollectedAt;
    private long joinDaysTillNow;
    private long postCountTillNow;
    private long accessCountTillNow;

}
