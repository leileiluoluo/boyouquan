package com.boyouquan.controller;

import com.boyouquan.constant.CommonConstants;
import com.boyouquan.model.Access;
import com.boyouquan.model.Blog;
import com.boyouquan.model.Post;
import com.boyouquan.service.AccessService;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PostService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

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

    @GetMapping("")
    public void go(@RequestParam("link") String link,
                   @RequestParam(value = "from", required = false) String from,
                   HttpServletRequest request, HttpServletResponse response) {
        try {
            if (StringUtils.isNotBlank(link)) {
                String ip = IpUtil.getRealIp(request);

                boolean success = saveAccessInfo(ip, link, from);
                if (success) {
                    // FIXME: important, use this way to solve path wth chinese character issue
                    link = new String(link.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                    response.sendRedirect(link);
                    return;
                }
            }

            response.sendRedirect(CommonConstants.HOME_PAGE_ADDRESS);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private boolean saveAccessInfo(String ip, String link, String from) throws URISyntaxException, MalformedURLException {
        Post post = postService.getByLink(link);
        String blogDomainName = "";
        if (null != post) {
            blogDomainName = post.getBlogDomainName();
        } else {
            String domainName = CommonUtils.getDomainFromURL(link);
            if (StringUtils.isNotBlank(domainName)) {
                Blog blog = blogService.getByDomainName(domainName);
                if (null != blog) {
                    blogDomainName = blog.getDomainName();
                }
            }
        }

        if (StringUtils.isBlank(blogDomainName)) {
            logger.error("blog domain name not found! link: {}, ip: {}", link, ip);
            return false;
        }

        // save
        Access access = new Access();
        access.setIp(ip);
        access.setLink(link);
        access.setBlogDomainName(blogDomainName);
        accessService.save(access);

        return true;
    }

}

