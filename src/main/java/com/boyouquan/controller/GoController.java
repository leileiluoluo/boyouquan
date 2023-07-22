package com.boyouquan.controller;

import com.boyouquan.model.Access;
import com.boyouquan.model.Blog;
import com.boyouquan.model.Post;
import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(GoController.class);

    @Autowired
    private BlogService blogService;
    @Autowired
    private PostService postService;
    @Autowired
    private AccessService accessService;

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
            logger.error(e.getMessage(), e);
        }
    }

    private void saveAccessInfo(String ip, String link) {
        Post post = postService.getByLink(link);
        if (null != post) {
            // save
            Access access = new Access();
            access.setLink(link);
            access.setBlogDomainName(post.getBlogDomainName());
            access.setIp(ip);
            accessService.save(access);
        } else {
            Blog blog = blogService.getByAddress(link);
            if (null != blog) {
                // save
                Access access = new Access();
                access.setLink(link);
                access.setBlogDomainName(blog.getDomainName());
                access.setIp(ip);
                accessService.save(access);
            }
        }
    }

}
