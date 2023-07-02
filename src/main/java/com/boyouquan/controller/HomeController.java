package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.BlogPost;
import com.boyouquan.service.BlogAccessService;
import com.boyouquan.service.BlogPostService;
import com.boyouquan.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private BlogPostService blogPostService;
    @Autowired
    private BlogAccessService blogAccessService;

    @GetMapping("")
    public String index(Model model) {
        return index(1, model);
    }

    @GetMapping("/page/{page}")
    public String index(@PathVariable("page") int page, Model model) {
        Pagination<BlogPost> pagination = blogPostService.listBlogPosts(page, CommonConstants.DEFAULT_PAGE_SIZE);

        model.addAttribute("pagination", pagination);
        model.addAttribute("totalBlogs", blogPostService.countBlogs());
        model.addAttribute("totalBlogPosts", blogPostService.countPosts());
        model.addAttribute("accessTotal", blogAccessService.totalCount());
        return "index";
    }

}
