package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.BlogPost;
import com.boyouquan.model.LatestNews;
import com.boyouquan.service.BlogAccessService;
import com.boyouquan.service.BlogPostService;
import com.boyouquan.service.LatestNewsService;
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
import java.util.List;

@Controller
@RequestMapping("")
public class HomeController {

    @Autowired
    private BlogPostService blogPostService;
    @Autowired
    private BlogAccessService blogAccessService;
    @Autowired
    private LatestNewsService latestNewsService;

    @GetMapping("")
    public void index(HttpServletResponse response) {
        try {
            response.sendRedirect("/home");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/home")
    public String home(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        return homeWithPagination(keyword, 1, model);
    }

    @GetMapping("/home/page/{page}")
    public String homeWithPagination(@RequestParam(value = "keyword", required = false) String keyword, @PathVariable("page") int page, Model model) {
        if (null == keyword) {
            keyword = "";
        }
        keyword = StringUtils.trim(keyword);

        Pagination<BlogPost> pagination = blogPostService.listBlogPosts(keyword, page, CommonConstants.DEFAULT_PAGE_SIZE);

        boolean hasLatestNews = false;
        List<LatestNews> latestNews = latestNewsService.getLatestNews();
        hasLatestNews = latestNews.size() > 1;

        model.addAttribute("pagination", pagination);
        model.addAttribute("totalBlogs", blogPostService.countBlogs(""));
        model.addAttribute("totalBlogPosts", blogPostService.countPosts(""));
        model.addAttribute("accessTotal", blogAccessService.totalCount());
        model.addAttribute("hasKeyword", StringUtils.isNotBlank(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("hasLatestNews", hasLatestNews);
        model.addAttribute("latestNews", latestNews);
        return "home";
    }

}
