package com.boyouquan.controller;

import com.boyouquan.model.BlogPost;
import com.boyouquan.service.RSSReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private RSSReaderService rssReaderService;

    @GetMapping("")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        List<BlogPost> blogPosts = rssReaderService.read("");
        model.addAttribute("blogPosts", blogPosts);
        return "index";
    }

}
