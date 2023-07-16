package com.boyouquan.controller;

import com.boyouquan.model.BlogInfo;
import com.boyouquan.model.DayAccess;
import com.boyouquan.service.BlogAccessService;
import com.boyouquan.service.BlogInfoService;
import com.boyouquan.service.BlogPostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/blogs")
public class BlogDetailController {

    @Autowired
    private BlogInfoService blogInfoService;
    @Autowired
    private BlogPostService blogPostService;
    @Autowired
    private BlogAccessService blogAccessService;

    @GetMapping("/{domain}/**")
    public String list(@PathVariable("domain") String domain, Model model, HttpServletRequest request) {
        // parse domain from request URL
        String requestURL = request.getRequestURL().toString();
        domain = requestURL.split("/blogs/")[1];

        // get blog info
        BlogInfo blogInfo = blogInfoService.getBlogInfoByDomain(domain);
        if (null == blogInfo) {
            return "error/404";
        }

        model.addAttribute("blogInfo", blogInfo);

        // for charts
        List<DayAccess> dayAccessList = blogAccessService.getBlogAccessSeriesInLatestOneMonth(blogInfo.getAddress());
        String[] monthlyAccessDataLabels = dayAccessList.stream().map(DayAccess::getDay).toArray(String[]::new);

        Integer[] monthlyAccessDataValues = dayAccessList.stream().map(DayAccess::getCount).toArray(Integer[]::new);

        model.addAttribute("monthlyAccessDataLabels", monthlyAccessDataLabels);
        model.addAttribute("monthlyAccessDataValues", monthlyAccessDataValues);

        // for summary
        model.addAttribute("totalBlogs", blogPostService.countBlogs(""));
        model.addAttribute("totalBlogPosts", blogPostService.countPosts(""));
        model.addAttribute("accessTotal", blogAccessService.totalCount());

        return "blog_detail/item";
    }

}
