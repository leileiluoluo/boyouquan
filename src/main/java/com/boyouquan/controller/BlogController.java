package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.BlogInfo;
import com.boyouquan.service.BlogInfoService;
import com.boyouquan.util.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogInfoService blogInfoService;

    @GetMapping("")
    public String list(Model model) {
        return list(1, model);
    }

    @GetMapping("/page/{page}")
    public String list(@PathVariable("page") int page, Model model) {
        Pagination<BlogInfo> pagination = blogInfoService.listBlogInfos(page, CommonConstants.DEFAULT_PAGE_SIZE);
        model.addAttribute("pagination", pagination);
        return "blogs/list";
    }

}
