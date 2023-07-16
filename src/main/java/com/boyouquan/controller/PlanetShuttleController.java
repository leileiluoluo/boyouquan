package com.boyouquan.controller;

import com.boyouquan.model.BlogAggregate;
import com.boyouquan.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("planet-shuttle")
public class PlanetShuttleController {

    @Autowired
    private BlogPostService blogPostService;

    @GetMapping("")
    public String shuttle(Model model) {
        BlogAggregate blogAggregate = blogPostService.getBlogByRandom();

        model.addAttribute("blogName", blogAggregate.getName());
        model.addAttribute("blogAddress", blogAggregate.getAddress());
        return "planet_shuttle/planet_shuttle";
    }

}
