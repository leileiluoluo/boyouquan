package com.boyouquan.controller;

import com.boyouquan.model.BlogLatestPublishedAt;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.List;

@Controller
public class SitemapController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;

    @GetMapping(value = "/sitemap.xml", produces = MediaType.TEXT_XML_VALUE)
    public String sitemap(Model model) {
        List<BlogLatestPublishedAt> blogLatestPublishedAts = blogService.listBlogLatestPublishedAt();

        model.addAttribute("now", CommonUtils.dateSitemapFormatStr(new Date()));
        model.addAttribute("blogLatestPublishedAts", blogLatestPublishedAts);

        return "sitemap/sitemap.xml";
    }

}
