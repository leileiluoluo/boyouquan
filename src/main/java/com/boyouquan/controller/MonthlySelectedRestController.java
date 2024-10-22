package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.MonthlySelectedPost;
import com.boyouquan.service.MonthlySelectedService;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/monthly-selected")
public class MonthlySelectedRestController {

    @Autowired
    private MonthlySelectedService monthlySelectedService;

    @GetMapping("")
    @Cacheable("monthlyReportCache")
    public Pagination<MonthlySelectedPost> list(@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        List<String> yearMonthStrs = monthlySelectedService.listYearMonthStrs();

        List<String> yearMonthSubStrs = yearMonthStrs.stream()
                .skip((long) (page - 1) * CommonConstants.MONTHLY_SELECTED_PAGE_SIZE)
                .limit(CommonConstants.MONTHLY_SELECTED_PAGE_SIZE)
                .toList();

        List<MonthlySelectedPost> monthlySelectedPosts = new ArrayList<>();

        yearMonthSubStrs.forEach(yearMonthStr -> {
            MonthlySelectedPost monthlySelected = monthlySelectedService.listSelectedByYearMonth(yearMonthStr);
            monthlySelectedPosts.add(monthlySelected);
        });

        return PaginationBuilder.<MonthlySelectedPost>newBuilder()
                .pageNo(page)
                .pageSize(CommonConstants.MONTHLY_SELECTED_PAGE_SIZE)
                .total((long) yearMonthStrs.size())
                .results(monthlySelectedPosts).build();
    }

}
