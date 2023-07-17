package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.NewBlogInfo;
import com.boyouquan.service.*;
import com.boyouquan.util.NewPagination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/blogs")
public class BlogListController {

    @Autowired
    private BlogService blogService;

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

        NewPagination<NewBlogInfo> blogInfoPagination = blogService.listBlogInfosWithKeyWord(keyword, page, CommonConstants.DEFAULT_PAGE_SIZE);
        model.addAttribute("pagination", blogInfoPagination);
        model.addAttribute("hasKeyword", StringUtils.isNotBlank(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalBlogs", 0);
        model.addAttribute("totalBlogPosts", 0);
        model.addAttribute("accessTotal", 0);
        return "blogs/list";
    }

}
