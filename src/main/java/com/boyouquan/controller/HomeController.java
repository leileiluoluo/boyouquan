package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.BlogPost;
import com.boyouquan.service.BlogAccessService;
import com.boyouquan.service.BlogPostService;
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
@RequestMapping("")
public class HomeController {

    @Autowired
    private BlogPostService blogPostService;
    @Autowired
    private BlogAccessService blogAccessService;

    @GetMapping("")
    public void index(HttpServletResponse response) {
        try {
            response.sendRedirect("/home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/home")
    public String home(Model model) {
        return homeWithPagination(1, model);
    }

    @GetMapping("/home/page/{page}")
    public String homeWithPagination(@PathVariable("page") int page, Model model) {
        Pagination<BlogPost> pagination = blogPostService.listBlogPosts(page, CommonConstants.DEFAULT_PAGE_SIZE);

        model.addAttribute("pagination", pagination);
        model.addAttribute("totalBlogs", blogPostService.countBlogs());
        model.addAttribute("totalBlogPosts", blogPostService.countPosts());
        model.addAttribute("accessTotal", blogAccessService.totalCount());
        return "home";
    }

}
