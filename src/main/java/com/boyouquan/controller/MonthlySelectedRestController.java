package com.boyouquan.controller;

import com.boyouquan.model.MonthlySelectedPost;
import com.boyouquan.service.MonthlySelectedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/monthly-selected")
public class MonthlySelectedRestController {

    @Autowired
    private MonthlySelectedService monthlySelectedService;

    @GetMapping("/all")
    public List<MonthlySelectedPost> all() {
        List<String> yearMonthStrs = monthlySelectedService.listYearMonthStrs();

        List<String> yearMonthSubStrs = yearMonthStrs.stream()
                .skip(0)
                .limit(2)
                .toList();

        List<MonthlySelectedPost> monthlySelectedPosts = new ArrayList<>();

        yearMonthSubStrs.forEach(yearMonthStr -> {
            MonthlySelectedPost monthlySelected = monthlySelectedService.listSelectedByYearMonth(yearMonthStr);
            monthlySelectedPosts.add(monthlySelected);
        });

        return monthlySelectedPosts;
    }

}
