package com.boyouquan.controller;

import com.boyouquan.model.BlogAccess;
import com.boyouquan.model.BlogAggregate;
import com.boyouquan.model.BlogPost;
import com.boyouquan.service.BlogAccessService;
import com.boyouquan.service.BlogPostService;
import com.boyouquan.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/go")
public class GoController {

    @Autowired
    private BlogAccessService blogAccessService;
    @Autowired
    private BlogPostService blogPostService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @GetMapping("")
    public void go(@RequestParam("link") String link, HttpServletRequest request, HttpServletResponse response) {
        try {
            String ip = IpUtil.getRealIp(request);

            // async
            executorService.execute(() -> saveAccessInfo(ip, link));

            // redirect
            response.sendRedirect(link);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAccessInfo(String ip, String link) {
        String address = link;

        // access blog post
        BlogPost blogPost = blogPostService.getBlogByLink(link);

        // access blog home
        if (null == blogPost) {
            BlogAggregate blogAggregate = blogPostService.getBlogByAddress(link);
            if (null != blogAggregate) {
                address = blogAggregate.getAddress();
            }
        } else {
            address = blogPost.getBlogAddress();
        }

        if (StringUtils.isBlank(address)) {
            System.out.printf("blog not found, link: %s", link);
            return;
        }

        // save
        BlogAccess blogAccess = new BlogAccess();
        blogAccess.setBlogAddress(address);
        blogAccess.setLink(link);
        blogAccess.setIp(ip);
        blogAccessService.saveBlogAccess(blogAccess);
    }

}
