package com.boyouquan.controller;

import com.boyouquan.model.BlogInfo;
import com.boyouquan.model.DayAccess;
import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PostService;
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
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;

    @GetMapping("/{domainName}/**")
    public String getBlogByDomainName(@PathVariable("domainName") String domainName, Model model, HttpServletRequest request) {
        // parse domain from request URL
        String requestURL = request.getRequestURL().toString();
        domainName = requestURL.split("/blogs/")[1];

        // get blog info
        BlogInfo blogInfo = blogService.getBlogInfoByDomainName(domainName);
        if (null == blogInfo) {
            return "error/404";
        }

        model.addAttribute("blogInfo", blogInfo);

        // for charts
        List<DayAccess> dayAccessList = accessService.getBlogAccessSeriesInLatestOneMonth(blogInfo.getDomainName());
        String[] monthlyAccessDataLabels = dayAccessList.stream().map(DayAccess::getDay).toArray(String[]::new);
        Integer[] monthlyAccessDataValues = dayAccessList.stream().map(DayAccess::getCount).toArray(Integer[]::new);

        model.addAttribute("monthlyAccessDataLabels", monthlyAccessDataLabels);
        model.addAttribute("monthlyAccessDataValues", monthlyAccessDataValues);

        // for summary
        model.addAttribute("totalBlogs", blogService.countAll());
        model.addAttribute("totalBlogPosts", postService.countAll());
        model.addAttribute("accessTotal", accessService.countAll());

        return "blog_detail/item";
    }

}
