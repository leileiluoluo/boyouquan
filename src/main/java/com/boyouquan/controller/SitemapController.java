package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.BlogLatestPublishedAt;
import com.boyouquan.model.PostLatestPublishedAt;
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
        List<PostLatestPublishedAt> postLatestPublishedAts = postService.listPostLatestPublishedAt(CommonConstants.SITEMAP_LATEST_POST_FETCH_SIZE);

        model.addAttribute("now", CommonUtils.dateSitemapFormatStr(new Date()));
        model.addAttribute("blogLatestPublishedAts", blogLatestPublishedAts);
        model.addAttribute("postLatestPublishedAts", postLatestPublishedAts);

        return "sitemap/sitemap.xml";
    }

}
