package com.boyouquan.controller;

import com.boyouquan.model.BlogInfo;
import com.boyouquan.service.BlogInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/blog-detail")
public class BlogDetailController {

    @Autowired
    private BlogInfoService blogInfoService;

    @GetMapping("/{domain}/**")
    public String list(@PathVariable("domain") String domain, Model model, HttpServletRequest request) {
        // parse domain from request URL
        String requestURL = request.getRequestURL().toString();
        domain = requestURL.split("/blog-detail/")[1];

        // get blog info
        BlogInfo blogInfo = blogInfoService.getBlogInfoByDomain(domain);
        model.addAttribute("blogInfo", blogInfo);

        return "blog_detail/item";
    }

}
