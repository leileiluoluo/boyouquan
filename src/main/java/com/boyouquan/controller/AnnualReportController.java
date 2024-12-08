package com.boyouquan.controller;

import com.boyouquan.service.AnnualReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/annual-reports")
public class AnnualReportController {

    @Autowired
    private AnnualReportService annualReportService;

    @GetMapping("")
    public ResponseEntity<String> getAnnualReport(@RequestParam("domainName") String domainName, @RequestParam("year") String year) {
        String annualReport = annualReportService.getAnnualReport(domainName, year);

        return ResponseEntity.ok(annualReport);
    }
}
