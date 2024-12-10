package com.boyouquan.service;

public interface AnnualReportService {

    String getAnnualReport(String domainName);

    void sendAnnualReport(String domainName);

}
