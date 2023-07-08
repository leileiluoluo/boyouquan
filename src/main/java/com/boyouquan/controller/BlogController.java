package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.BlogInfo;
import com.boyouquan.service.BlogInfoService;
import com.boyouquan.util.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogInfoService blogInfoService;

    @GetMapping("")
    public String list(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        return list(keyword, 1, model);
    }

    @GetMapping("/page/{page}")
    public String list(@RequestParam(value = "keyword", required = false) String keyword, @PathVariable("page") int page, Model model) {
        if (null == keyword) {
            keyword = "";
        }
        keyword = StringUtils.trim(keyword);

        Pagination<BlogInfo> pagination = blogInfoService.listBlogInfos(keyword, page, CommonConstants.DEFAULT_PAGE_SIZE);
        model.addAttribute("pagination", pagination);
        model.addAttribute("hasKeyword", StringUtils.isNotBlank(keyword));
        model.addAttribute("keyword", keyword);
        return "blogs/list";
    }

}
