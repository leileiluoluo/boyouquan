package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.MonthlySelectedPost;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.BlogStatusService;
import com.boyouquan.service.MonthlySelectedService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.Pagination;
import com.boyouquan.util.PaginationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/monthly-selected")
public class MonthlySelectedController {

    @Autowired
    private MonthlySelectedService monthlySelectedService;
    @Autowired
    private PostService postService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogStatusService blogStatusService;

    @GetMapping("")
    public String list(Model model) {
        return list(1, model);
    }

    @GetMapping("/page/{page}")
    public String list(@PathVariable("page") int page, Model model) {
        List<String> yearMonthStrs = monthlySelectedService.listYearMonthStrs()
                .stream()
                .skip((long) (page - 1) * CommonConstants.MONTHLY_SELECTED_PAGE_SIZE)
                .limit(CommonConstants.MONTHLY_SELECTED_PAGE_SIZE)
                .toList();

        List<MonthlySelectedPost> monthlySelectedPosts = new ArrayList<>();

        yearMonthStrs.forEach(yearMonthStr -> {
            MonthlySelectedPost monthlySelected = monthlySelectedService.listSelectedByYearMonth(yearMonthStr);
            monthlySelectedPosts.add(monthlySelected);
        });

        Pagination<MonthlySelectedPost> pagination = PaginationBuilder.<MonthlySelectedPost>newBuilder()
                .pageNo(page)
                .pageSize(CommonConstants.MONTHLY_SELECTED_PAGE_SIZE)
                .results(monthlySelectedPosts).build();

        model.addAttribute("pagination", pagination);

        return "monthly_selected/list";
    }

}
