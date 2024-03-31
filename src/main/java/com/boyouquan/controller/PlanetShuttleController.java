package com.boyouquan.controller;

import com.boyouquan.model.Blog;
import com.boyouquan.service.BlogService;
import com.boyouquan.util.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequestMapping("planet-shuttle")
public class PlanetShuttleController {

    @Autowired
    private BlogService blogService;

    @GetMapping("")
    public String shuttle(HttpServletRequest request, Model model) {
        String referer = request.getHeader("Referer");
        Blog fromBlog = null;
        if (StringUtils.isNotBlank(referer)) {
            String fromDomainName = CommonUtils.getDomainFromURL(referer);
            fromBlog = blogService.getByDomainName(fromDomainName);
        }

        Blog blog = blogService.listByRandom(Collections.emptyList(), 1).stream().findFirst().get();

        model.addAttribute("blogName", blog.getName());
        model.addAttribute("blogAddress", blog.getAddress());
        model.addAttribute("fromBlog", fromBlog);

        return "planet_shuttle/planet_shuttle";
    }

}
