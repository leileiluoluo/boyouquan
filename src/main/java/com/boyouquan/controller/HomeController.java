package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.BlogPost;
import com.boyouquan.service.BlogPostService;
import com.boyouquan.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private BlogPostService blogPostService;

    @GetMapping("")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        Pagination<BlogPost> pagination = blogPostService.listBlogPosts(page, CommonConstants.DEFAULT_PAGE_SIZE);
        model.addAttribute("pagination", pagination);
        return "index";
    }

}
