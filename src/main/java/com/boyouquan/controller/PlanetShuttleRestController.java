package com.boyouquan.controller;

import com.boyouquan.model.Blog;
import com.boyouquan.model.PlanetShuttle;
import com.boyouquan.service.BlogService;
import com.boyouquan.service.PlanetShuttleService;
import com.boyouquan.util.CommonUtils;
import com.boyouquan.util.IPUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/planet-shuttle")
public class PlanetShuttleRestController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PlanetShuttleService planetShuttleService;

    @GetMapping("")
    public Map<String, Object> shuttle(HttpServletRequest request, Model model) {
        Map<String, Object> result = new HashMap<>();

        String referer = request.getHeader("Referer");
        Blog fromBlog = null;
        if (StringUtils.isNotBlank(referer)) {
            String fromDomainName = CommonUtils.getDomainFromURL(referer);
            fromBlog = blogService.getByShortDomainName(fromDomainName);
        }

        Blog blog = blogService.listByRandom(Collections.emptyList(), 1).stream().findFirst().get();

        // save planet shuttle
        long fromBlogInitiatedCount = 0;
        if (null != fromBlog) {
            String ip = IPUtil.getRealIp(request);

            PlanetShuttle planetShuttle = new PlanetShuttle();
            planetShuttle.setIp(ip);
            planetShuttle.setBlogDomainName(fromBlog.getDomainName());
            planetShuttle.setToBlogAddress(blog.getAddress());
            planetShuttleService.save(planetShuttle, referer, fromBlog, blog);

            fromBlogInitiatedCount = planetShuttleService.countInitiatedByBlogDomainName(fromBlog.getDomainName());
        }

        result.put("blogName", blog.getName());
        result.put("blogAddress", blog.getAddress());
        result.put("fromBlog", fromBlog);
        result.put("fromBlogInitiatedCount", fromBlogInitiatedCount);

        return result;
    }

}
