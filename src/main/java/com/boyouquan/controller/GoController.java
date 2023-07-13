package com.boyouquan.controller;

import com.boyouquan.model.BlogAccess;
import com.boyouquan.model.BlogPost;
import com.boyouquan.service.BlogAccessService;
import com.boyouquan.service.BlogPostService;
import com.boyouquan.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        // access blog post
        BlogPost blogPost = blogPostService.getBlogByLink(link);

        // access blog home
        if (null == blogPost) {
            blogPost = blogPostService.getBlogByAddress(link);
        }

        if (null == blogPost) {
            System.out.printf("blog not found, link: %s", link);
            return;
        }

        BlogAccess blogAccess = new BlogAccess();
        blogAccess.setBlogAddress(blogPost.getBlogAddress());
        blogAccess.setLink(link);
        blogAccess.setIp(ip);
        blogAccessService.saveBlogAccess(blogAccess);
    }

}
